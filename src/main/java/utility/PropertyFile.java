package utility;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertyFile {
	
	static Properties properties = new Properties();
	
	public static Properties load(String filePath) {
		
		FileInputStream fs;
		try {
			fs = new FileInputStream(new File(filePath));
			properties.load(fs);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return properties;
		
	}

}
