package jds.bibliocraft.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import jds.bibliocraft.Config;
import jds.bibliocraft.items.ItemBigBook;
import jds.bibliocraft.items.ItemRecipeBook;
import jds.bibliocraft.items.ItemStockroomCatalog;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class FileUtil {
	private final String savePath = "books_bibliocraft";

	public boolean isBookSaved(ItemStack stack, World world) {
		if (stack.hasTagCompound()) {
			NBTTagCompound pageTags = stack.getTagCompound();

			if (stack.getItem() instanceof ItemBigBook) {
				String filename = "";
				String title = stack.getDisplayName();
				title = TextFormatting.getTextWithoutFormattingCodes(title);
				String author = pageTags.getString("author");
				author = TextFormatting.getTextWithoutFormattingCodes(author);
				filename = author + ", " + title;
				File book = new File(getSaveDir(world), filename);
				if (book.exists()) {
					return true;
				}
			} else if (stack.getItem() instanceof ItemRecipeBook || stack.getItem() instanceof ItemStockroomCatalog) {
				String title = stack.getDisplayName();
				title = TextFormatting.getTextWithoutFormattingCodes(title);
				title = title.replace(":", ";");
				File book = new File(getSaveDir(world), title);
				if (book.exists()) {
					return true;
				}
			} else {

				NBTTagString titleTag = (NBTTagString) pageTags.getTag("title");
				NBTTagString authorTag = (NBTTagString) pageTags.getTag("author");
				if (titleTag != null && authorTag != null) {
					String title = titleTag.toString();
					String author = authorTag.toString();
					title = title.replace("\"", "");
					author = author.replace("\"", "");
					// System.out.println(title);
					// System.out.println(author);
					NBTTagList page = (NBTTagList) pageTags.getTagList("pages", 8);

					File book = new File(getSaveDir(world), author + ", " + title + "");
					if (book.exists()) {
						return true;
					}
				}
			}
		}
		return false;

	}

	/**
	 * Saves a book from a recieved itemstack to the external save folder for books.
	 * 
	 * @param stack
	 * @param world
	 */
	public void saveBook(ItemStack stack, World world) {
		// System.out.println(world.getSaveHandler().getSaveDirectoryName());
		if (stack.hasTagCompound()) {
			NBTTagCompound pageTags = stack.getTagCompound();
			NBTTagString titleTag = (NBTTagString) pageTags.getTag("title");
			NBTTagString authorTag = (NBTTagString) pageTags.getTag("author");
			if (titleTag != null && authorTag != null) {
				String title = titleTag.toString();
				String author = authorTag.toString();
				title = title.replace("\"", "");
				author = author.replace("\"", "");
				title = TextFormatting.getTextWithoutFormattingCodes(title);
				author = TextFormatting.getTextWithoutFormattingCodes(author);
				NBTTagList page = (NBTTagList) pageTags.getTagList("pages", 8);
				File book = new File(getSaveDir(world), author + ", " + title + "");
				if (book.exists()) {
					// System.out.println("Book already exsists!");
				} else {
					// System.out.println("creating a filewriter..");
					try {
						FileWriter writer = new FileWriter(book);
						writer.write(title);
						writer.write("\n");
						writer.write(author);
						writer.write("\n");
						if (Config.enablePublicTypesettingBooks) {
							writer.write("public");
						} else {
							writer.write("private");
						}
						writer.write("\n");

						for (int i = 0; i < page.tagCount(); i++) {
							// System.out.println(page.tagAt(i));
							writer.write("#pgx" + i);
							writer.write("\n");
							writer.write(page.getStringTagAt(i));
							writer.write("\n");
						}

						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				// max of 247 characters per page
				// 19 characters per line
				// 13 lines per page

			}

		}

	}

	/**
	 * Returns an int from the array of books that can be found in the stored
	 * folder.
	 * Uses the parameters author and title to search the names of the saved books
	 * after
	 * storing a list to an array.
	 * 
	 * @param world
	 * @param author
	 * @param title
	 * @return
	 */
	public int getBookNumber(World world, String author, String title) {
		String[] booksArray = scanBookDir(world);
		int bookint = -1;
		if (booksArray != null) {
			for (int x = 0; x < booksArray.length; x++) {
				if (booksArray[x].contains(author) && booksArray[x].contains(title)) {
					bookint = x;
				}
			}

		}
		return bookint;
	}

	public int getBookNumber(World world, String authortitle) {
		String[] booksArray = scanBookDir(world);
		int bookint = -1;
		if (booksArray != null) {
			for (int x = 0; x < booksArray.length; x++) {
				if (booksArray[x].contains(authortitle)) {
					bookint = x;
				}
			}

		}
		return bookint;
	}

	public int getBookNumber(boolean isclient, String authortitle) {
		String[] booksArray = scanBookDir(isclient);
		int bookint = -1;
		if (booksArray != null) {
			for (int x = 0; x < booksArray.length; x++) {
				if (booksArray[x].contains(authortitle)) {
					bookint = x;
				}
			}

		}
		return bookint;
	}

	/**
	 * Takes an item stack and if it is the correct item, it will convert the item
	 * to
	 * a book that contains the information saved to a book on file. The chosen book
	 * from file is indicated by the iteger of the array that books are stored in.
	 * 
	 * @param world
	 * @param stack
	 * @param bookint
	 * @return
	 */

	public ItemStack loadBook(World world, ItemStack stack, int bookint) {
		if (stack.getItem() != Items.BOOK) {
			System.out.println("Cannot print to these type of book");
			return ItemStack.EMPTY;
		}
		String[] booksArray = scanBookDir(world);

		if (booksArray != null) {
			if (booksArray.length < bookint) {
				// System.out.println("Book scan turned up empty");
				return ItemStack.EMPTY;
			}
			// System.out.println("is this happening?");
			stack = new ItemStack(Items.WRITTEN_BOOK);
			File book = new File(getSaveDir(world), booksArray[bookint]);
			NBTTagCompound bookinfo = new NBTTagCompound();
			try {
				FileReader reader = new FileReader(book);
				BufferedReader breader = new BufferedReader(reader);
				// this totally work, it reads the book and goes through the entire thing, line
				// by line.
				String line;
				line = breader.readLine();
				// System.out.println("I'm ok 1");
				if (line != null) {
					bookinfo.setTag("title", new NBTTagString(line));
					line = breader.readLine();
					bookinfo.setTag("author", new NBTTagString(line));
					line = breader.readLine();
				}
				// I am most likely going to have to put in some checks to for line length and
				// skip lines that are too long?, will test, if minecraft auto-skips, thats
				// easier
				// but we cant have any crashes
				line = breader.readLine();
				String pagetag;
				NBTTagList bookTagList = new NBTTagList(); // ised to have "pages"
				while (line != null) {
					if (!line.contains("#pgx") && line != null) {
						pagetag = line;
						line = breader.readLine();
						if (line != null) {
							if (line.isEmpty()) {
								line = " ";
							}
							while (!line.contains("#pgx") && line != null) {
								pagetag = pagetag + "\n" + line;
								line = breader.readLine();
								if (line == null) {
									break;
								}
								if (line.isEmpty()) {
									line = " ";
								}
							}
						}
						NBTTagString nbtpage = new NBTTagString(pagetag);
						bookTagList.appendTag(nbtpage);

						bookinfo.setTag("pages", bookTagList);
						byte resolveByte = 1;
						NBTTagByte resolve = new NBTTagByte(resolveByte);
						bookinfo.setTag("resolved", resolve);
					} else {
						line = breader.readLine();
					}

				}
				breader.close();
				// System.out.println(bookTagList.tagCount());
				stack.setTagCompound(bookinfo);
				return stack;

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			// System.out.println("No books found!");
			return ItemStack.EMPTY;
		}
		return ItemStack.EMPTY;

		// File book = new File(getSaveDir(world), author+"_"+title+".dat");
		// FileReader reader = new FileReader(book)
	}

	/**
	 * Scans the directory of saved books and returns a string array of all the
	 * saved books.
	 * 
	 * @param world
	 * @return
	 */

	public String[] scanBookDir(World world) {
		File bookLoc = getSaveDir(world); // new File(getSaveDir(world), "");
		File[] bookList = bookLoc.listFiles();
		if (bookList != null) {
			String[] bookArray = new String[bookList.length];
			for (int x = 0; x < bookList.length; x++) {
				String bookName = bookList[x].getName();
				bookArray[x] = bookList[x].getName();
			}

			ArrayList bookSortList = new ArrayList();
			for (int n = 0; n < bookArray.length; n++) {
				if (!(bookArray[n].contains(".dat"))) {
					bookSortList.add(bookArray[n]);
					// System.out.println(bookArray[n]);
				}
			}
			String[] newBookArray = new String[bookSortList.size()];
			for (int n = 0; n < newBookArray.length; n++) {

				newBookArray[n] = (String) bookSortList.get(n);
			}
			bookArray = newBookArray;
			return bookArray;
		} else {
			return null;
		}
	}

	/**
	 * Returns the save directory of the books in a File object.
	 * 
	 * @param world
	 * @return
	 */

	public File getSaveDir(World world) {
		File storage;
		if (FMLCommonHandler.instance().getMinecraftServerInstance().toString().contains("integrated")) {
			// Client / Integrated server
			storage = new File(Minecraft.getMinecraft().mcDataDir, "config");
		} else {
			// Dedicated Server
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			storage = new File(server.getFolderName());
		}

		File biblioDir = new File(storage, savePath);
		if (biblioDir.mkdir()) {
			// I am thinking there would be the place to copy in and write in included books
		}

		return biblioDir;
	}

	public String[] scanBookDir(boolean isclient) {
		File bookLoc = getSaveDir(isclient);
		File[] bookList = bookLoc.listFiles();
		if (bookList != null) {
			String[] bookArray = new String[bookList.length];
			for (int x = 0; x < bookList.length; x++) {
				String bookName = bookList[x].getName();
				bookArray[x] = bookList[x].getName();
			}
			ArrayList bookSortList = new ArrayList();
			for (int n = 0; n < bookArray.length; n++) {
				bookSortList.add(bookArray[n]);
			}
			for (int n = 0; n < bookSortList.size(); n++) {
				String bname = (String) bookSortList.get(n);
				if (bname.contains(".dat")) {
					bookSortList.remove(n);
				}
			}
			String[] newBookArray = new String[bookSortList.size()];
			for (int n = 0; n < newBookArray.length; n++) {

				newBookArray[n] = (String) bookSortList.get(n);
			}
			bookArray = newBookArray;
			return bookArray;
		} else {
			return null;
		}

	}

	public String getBookName(World world, int booknum) {
		String[] booklist = scanBookDir(world);
		if (booklist.length > booknum) {
			return booklist[booknum];
		}
		return null;
	}

	public String[] getAuthorList(String[] books, boolean isClient) {
		File bookLoc = getSaveDir(isClient); // new File(getSaveDir(world), "");
		// File[] bookList = bookLoc.listFiles();
		String[] authorList = new String[books.length];
		for (int i = 0; i < books.length; i++) {
			try {
				File book = new File(bookLoc, books[i]);
				FileReader reader;
				reader = new FileReader(book);
				BufferedReader breader = new BufferedReader(reader);
				String line;
				line = breader.readLine();
				line = breader.readLine();
				if (line != null) {
					// This should be the author
					authorList[i] = line;
				}
				breader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return authorList;
	}

	public boolean[] getPublistList(String[] books, boolean isClient) {
		File bookLoc = getSaveDir(isClient);
		boolean[] isPublicList = new boolean[books.length];
		for (int i = 0; i < books.length; i++) {
			try {
				File book = new File(bookLoc, books[i]);
				FileReader reader;
				reader = new FileReader(book);
				BufferedReader breader = new BufferedReader(reader);
				String line;
				line = breader.readLine();
				if (line != null)
					line = breader.readLine();
				if (line != null)
					line = breader.readLine();
				if (line != null) {
					if (line.contains("private")) {
						isPublicList[i] = false;
					} else if (line.contains("public")) {
						isPublicList[i] = true;
					} else {
						// update private/public field
						addPublicPrivateFieldToBook(bookLoc, books[i]);
						isPublicList[i] = true;
					}
				}
				breader.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return isPublicList;
	}

	public void addPublicPrivateFieldToBook(File dir, String bookFile) {
		ArrayList bookText = new ArrayList();
		try {
			File book = new File(dir, bookFile);
			FileReader reader;
			reader = new FileReader(book);
			BufferedReader breader = new BufferedReader(reader);
			String line;
			line = breader.readLine();
			if (line != null)
				bookText.add(line);
			line = breader.readLine();
			if (line != null)
				bookText.add(line);
			line = breader.readLine();
			if (line != null) {
				if (line.contains("private") || line.contains("public")) {
					bookText.add(line);
				} else {
					bookText.add("public");
				}
			}
			line = breader.readLine();
			while (line != null) {
				bookText.add(line);
				line = breader.readLine();
			}
			breader.close();
			FileWriter writer = new FileWriter(book);
			for (int i = 0; i < bookText.size(); i++) {
				// System.out.println(page.tagAt(i));
				writer.write((String) bookText.get(i));
				writer.write("\n");
			}
			writer.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public File getSaveDir(boolean isclient) {
		File storage;
		if (isclient) {
			// Client
			storage = new File(Minecraft.getMinecraft().mcDataDir, "config");
		} else {
			// Server
			MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
			storage = new File(server.getFolderName());
		}
		File biblioDir = new File(storage, savePath);
		// System.out.println("make dir");
		if (biblioDir.mkdir()) {
			// System.out.println("Just made the directory");
			// I am thinking there would be the place to copy in and write in included books
		}

		return biblioDir;
	}

	public boolean deleteBook(boolean isClient, String bookname) {
		bookname = TextFormatting.getTextWithoutFormattingCodes(bookname);
		File saveDir = getSaveDir(isClient);
		File book = new File(saveDir, bookname);
		if (book.getParentFile().getAbsolutePath().equals(saveDir.getAbsolutePath())) {
			int bookCheck = getBookType(isClient, getBookNumber(isClient, bookname));
			if (bookCheck == -1) {
				if (book.delete()) {
					return true;
				}
			} else {
				File bookDat = new File(getSaveDir(isClient), bookname + ".dat");
				if (bookDat.getParentFile().getAbsolutePath().equals(saveDir.getAbsolutePath())) {
					if (book.delete() && bookDat.delete()) {
						return true;
					}
				}
			}
		}
		// System.out.println("ready to delete!");
		return false;
	}

	public boolean updatePublicFlag(boolean isClient, String bookname, boolean flag) {
		ArrayList bookText = new ArrayList();
		try {
			File saveDir = getSaveDir(isClient);
			File book = new File(saveDir, bookname);
			if (book.getParentFile().getAbsolutePath().equals(saveDir.getAbsolutePath())) { // client might send a
																							// bookname with `..` to
																							// write outside of the
																							// folder
				FileReader reader;
				reader = new FileReader(book);
				BufferedReader breader = new BufferedReader(reader);
				String line;
				line = breader.readLine();
				if (line != null)
					bookText.add(line);
				line = breader.readLine();
				if (line != null)
					bookText.add(line);
				line = breader.readLine();
				if (line != null) {
					if (flag) {
						bookText.add("public");
					} else {
						bookText.add("private");
					}
				}
				line = breader.readLine();
				while (line != null) {
					bookText.add(line);
					line = breader.readLine();
				}
				breader.close();
				FileWriter writer = new FileWriter(book);
				for (int i = 0; i < bookText.size(); i++) {
					// System.out.println(page.tagAt(i));
					writer.write((String) bookText.get(i));
					writer.write("\n");
				}
				writer.close();
				return true;
			}
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 
	 * @param book
	 * @param world
	 * @param filename
	 * @param type;    0 = BigBook, 1 = Recipe book, more to follow eventually?
	 * @return
	 */
	public boolean saveNBTtoFile(ItemStack book, World world, int type) {
		if (book != ItemStack.EMPTY) {
			NBTTagCompound nbt = book.getTagCompound();
			if (nbt != null) {

				String filename = "";
				String title = book.getDisplayName();
				title = TextFormatting.getTextWithoutFormattingCodes(title);
				boolean savedMeta = false;
				if (type == 0) {
					String author = nbt.getString("author");
					author = TextFormatting.getTextWithoutFormattingCodes(author);
					filename = author + ", " + title;
					savedMeta = saveBookMeta(world, filename, title, author, "Big");
					// need to save bookname, author, private, type, filename

				} else if (type == 1) {
					title = title.replace(":", ";");
					filename = title;
					savedMeta = saveBookMeta(world, filename, title, "NoAuthor", "Recipe");
				} else if (type == 2) {
					title = title.replace(":", ";");
					filename = title;
					savedMeta = saveBookMeta(world, filename, title, "NoAuthor", "Stockroom");
				} else {
					return false;
				}
				// System.out.println(filename);

				if (savedMeta) {
					String metaname = filename + ".dat";
					metaname = metaname.replace("..", ".");
					File saveDir = getSaveDir(world);
					File bookFile = new File(saveDir, metaname);
					if (this.isWithin(saveDir, bookFile)) {
						try {
							CompressedStreamTools.write(nbt, bookFile);
							return true;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}

		}
		return false;
	}

	private boolean isWithin(File dir, File file) {
		return file.getParentFile().getAbsolutePath().equals(dir.getAbsolutePath());
	}

	public boolean saveBookMeta(World world, String filename, String title, String author, String type) {
		File saveDir = getSaveDir(world);
		File bookFile = new File(saveDir, filename);
		if (!bookFile.exists() && this.isWithin(saveDir, bookFile)) {
			try {
				FileWriter writer = new FileWriter(bookFile);
				writer.write(title);
				writer.write("\n");
				writer.write(author);
				writer.write("\n");
				if (Config.enablePublicTypesettingBooks) {
					writer.write("public");
				} else {
					writer.write("private");
				}
				writer.write("\n");
				writer.write("Booktype&=" + type);
				writer.write("\n");
				writer.write(filename);
				// writer.write("\n");
				writer.close();
				return true;

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * Return the book type
	 * -1 = vanilla, 0 = big book and 1 = recipe book
	 * 
	 * @param world
	 * @param bookNum
	 * @return
	 */
	public int getBookType(World world, int bookNum) {
		String[] booksArray = scanBookDir(world);
		try {
			File saveDir = getSaveDir(world);
			File book = new File(saveDir, booksArray[bookNum]);
			if (!this.isWithin(saveDir, book)) {
				return -1;
			}
			FileReader reader;
			reader = new FileReader(book);
			BufferedReader breader = new BufferedReader(reader);
			String line;
			line = breader.readLine(); // author
			line = breader.readLine(); // title
			line = breader.readLine(); // permission
			line = breader.readLine(); // type
			if (line != null) {
				if (line.contains("Booktype=")) {
					line = line.replace("Booktype=", "");
				}
			}
			breader.close();
			if (line.contains("Big")) {
				return 0;
			} else if (line.contains("Recipe")) {
				return 1;
			} else if (line.contains("Stockroom")) {
				return 2;
			}

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return -1;
	}

	public int getBookType(boolean isClient, int bookNum) {
		String[] booksArray = scanBookDir(isClient);
		try {
			File saveDir = getSaveDir(isClient);
			File book = new File(saveDir, booksArray[bookNum]);
			if (!this.isWithin(saveDir, book)) {
				return -1;
			}
			FileReader reader;
			reader = new FileReader(book);
			BufferedReader breader = new BufferedReader(reader);
			String line;
			line = breader.readLine(); // author
			line = breader.readLine(); // title
			line = breader.readLine(); // permission
			line = breader.readLine(); // type
			if (line != null) {
				if (line.contains("Booktype=")) {
					line = line.replace("Booktype=", "");
				}
			}
			breader.close();
			if (line.contains("Big")) {
				return 0;
			} else if (line.contains("Recipe")) {
				return 1;
			}

			else if (line.contains("Stockroom")) {
				return 2;
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return -1;
	}

	public ItemStack loadBookNBT(World world, ItemStack bookstack, int booknum) {
		String[] booklist = scanBookDir(world);
		File saveDir = getSaveDir(world);
		File book = new File(saveDir, booklist[booknum] + ".dat");
		if (this.isWithin(saveDir, book)) {
			NBTTagCompound nbt = null;
			// System.out.println("testing: "+book.exists());
			if (book.exists()) {
				try {
					nbt = CompressedStreamTools.read(book);
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (nbt != null) {
					bookstack.setTagCompound(nbt);
				}
			}
		}
		return bookstack;
	}
}