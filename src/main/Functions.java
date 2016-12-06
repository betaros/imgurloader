package main;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.sangupta.imgur.api.ImgurClient;
import com.sangupta.imgur.api.model.Album;
import com.sangupta.imgur.api.model.Album.AlbumWrapper;
import com.sangupta.imgur.api.model.Image;

import main.Debug;
import utils.CBZcreator;
import utils.PDFcreator;

public class Functions {
	private ImgurClient client;
	private Debug debug;
	private String path;
	private String fullpath;

	/**
	 * 
	 * @param profile
	 */
	public Functions(Profile profile){
		this.client = new ImgurClient(profile.getId(), profile.getSecret());
		this.debug = new Debug(this.getClass().getName());
		this.path = System.getProperty("user.dir");
		debug.DebugMessage("Current Path:		" + this.path);
	}

	/**
	 * 
	 * @param albumID
	 */
	public void download(String albumID){
		debug.DebugMessage(albumID);
		
		StringBuilder sbFullpath = new StringBuilder();
		sbFullpath.append(this.path);
		sbFullpath.append("\\");
		sbFullpath.append(albumID);
		this.fullpath = sbFullpath.toString();
		
		createTempFolder(albumID);
		
		AlbumWrapper aw = client.getAlbumDetails(albumID);
		if(aw.success){
			debug.DebugMessage("AlbumWrapper:		" + aw.toString());
			Album album = aw.data;
			List<Image> imageList = album.images;
						
			int counter = 1;
			int zerocount = (int)Math.log10(imageList.size());
			for(Image i:imageList){
				debug.DebugMessage("Imagelink:		" + i.link);
				StringBuilder number = new StringBuilder();
				for(int j = (int)Math.log10(counter); j<zerocount; j++){
					number.append("0");
				}
				number.append(String.valueOf(counter));
				String filetype = i.link.substring(i.link.lastIndexOf(".") + 1).trim();
				File f = new File(this.fullpath + "\\" + number.toString() + "." + filetype);
				URL url;
				try {
					url = new URL(i.link);
					FileUtils.copyURLToFile(url, f);
					counter++;
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void generator(String albumID, boolean pdfbool, boolean cbzbool){
		Thread pdf = new Thread(new PDFcreator(fullpath, client.getAlbumDetails(albumID).data.title + ".pdf"));
		Thread cbz = new Thread(new CBZcreator(fullpath, client.getAlbumDetails(albumID).data.title + ".cbz"));
		
		if(pdfbool){
			pdf.run();
		}
		
		if(cbzbool){
			cbz.run();
		}
	}
	
	/**
	 * 
	 * @param albumID
	 * @return
	 */
	public boolean createTempFolder(String albumID){
		File f = new File(fullpath);
		try{
			if(f.mkdir()) { 
				debug.DebugMessage("Path:			" + f.getAbsolutePath());
				debug.DebugMessage("Directory created");
				return true;
			} else {
				debug.DebugMessage("Path:			" + f.getAbsolutePath());
				debug.DebugMessage("Directory is not created");
				return false;
			}
		} catch(Exception e){
			e.printStackTrace();
			return false;
		} 
	}
	
	/**
	 * 
	 * @param albumID
	 * @return
	 */
	public boolean removeTempFolder(String albumID){
		File f = new File(fullpath.toString());
		try {
			FileUtils.deleteDirectory(f);
			debug.DebugMessage("Path:			" + f.getAbsolutePath());
			debug.DebugMessage("Directory deleted");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
	/**
	 * 
	 * @param albumID
	 * @return
	 */
	public String getTitle(String albumID){
		return client.getAlbumDetails(albumID).data.title;
	}
	
	/**
	 * 
	 * @param albumID
	 * @return
	 */
	public String getPictureCount(String albumID){
		return String.valueOf(client.getAlbumDetails(albumID).data.imagesCount);
	}
}
