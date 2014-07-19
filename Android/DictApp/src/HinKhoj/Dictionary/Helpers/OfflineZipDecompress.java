package HinKhoj.Dictionary.Helpers;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import HinKhoj.Dictionary.Constants.AppConstants;
import android.util.Log;
import android.widget.ProgressBar;

/** 
 * 
 * @author jon 
 */ 
public class OfflineZipDecompress { 
	private String _zipFile; 
	private String _location; 
	private ProgressBar progressManager;
	private static int TOTAL_SIZE=AppConstants.DBFileSize;

	public OfflineZipDecompress(String zipFile, String location,ProgressBar progressManager) { 
		_zipFile = zipFile; 
		_location = location; 
		this.progressManager=progressManager;
		_dirChecker(""); 
	} 

	public void unzip() { 
		try  {  
			FileInputStream fin= new FileInputStream(_zipFile);
			ZipInputStream zin = new ZipInputStream(fin); 
			ZipEntry ze = null; 
			int counter=0;
			int bridge=900;
			while ((ze = zin.getNextEntry()) != null) { 

				if(ze.isDirectory()) { 
					_dirChecker(ze.getName()); 
				} else { 
					FileOutputStream fout = new FileOutputStream(_location +"/"+ ze.getName()); 
					byte[] buffer = new byte[10240];
					int read;
					while((read = zin.read(buffer)) != -1){
						fout.write(buffer, 0, read);
						counter+=read;
			              if(counter%10240==0)
			              {
			          
							int updateCounter=counter*100/TOTAL_SIZE;
							updateCounter=updateCounter/4;
							updatePM(updateCounter);
			              }

					}
					zin.closeEntry(); 
					fout.close(); 
				} 

			} 
			zin.close(); 
			dismissPM();

		} catch(Exception e) { 
			dismissPM();
		} 

	} 
	
	private void dismissPM() {
		// TODO Auto-generated method stub
		if(progressManager!=null)
		{
			progressManager.setProgress(50);
		}
	}

	private void updatePM(int updateCounter)
	{
		if(progressManager!=null)
		{
			if(updateCounter>90)
			{
				progressManager.setProgress(90);
			}
			else
			{
				progressManager.setProgress(updateCounter);
			}
		}
	}

	private void _dirChecker(String dir) { 
		File f = new File(_location + "/"+dir); 

		if(!f.isDirectory()) { 
			f.mkdirs(); 
		} 
	} 
} 