package org.esupportail.portlet.stockage.services;

import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.esupportail.commons.utils.Base64;
import org.esupportail.portlet.stockage.beans.JsTreeFile;

/**
 * This Class provides encode/decode methods for path string
 * so that we have a two-way converter between a path and a string/id
 * using only alphanumeric char, '_' char and '-' char.
 * This id can be used with jquery, like id of html tag, etc.
 * Moreover this id desn't need specific encoding characters for accents 
 * (because we don't use accents).
 */
public class PathEncodingUtils {

	protected static final Log log = LogFactory.getLog(PathEncodingUtils.class);

	protected static final String PREFIX_CODE = "path_";
	
	public static String encodeDir(String path) {
		if(path == null)
			return null;
		String encodedPath = path;
		encodedPath = Base64.encodeBytes(path.getBytes(), Base64.URL_SAFE);
		encodedPath = encodedPath.replaceAll("\n", "");
		encodedPath = encodedPath.replaceAll("=", "");
		return PREFIX_CODE + encodedPath;
	}
	
	public static String decodeDir(String dir) {
		if(dir == null || "".equals(dir))
			return null;
		dir = dir.substring(PREFIX_CODE.length());
		int nb_equals_to_add = 4 - dir.length() % 4;
		if(nb_equals_to_add == 1)
			dir = dir + "=";
		if(nb_equals_to_add == 2)
			dir = dir + "==";
		dir = new String(Base64.decode(dir, Base64.URL_SAFE));
		return dir;
	}

	public static List<String> decodeDirs(List<String> dirs) {
		if(dirs == null)
			return null;
		List<String> decodedDirs = new Vector<String>(dirs.size());
		for(String dir: dirs)
			decodedDirs.add(decodeDir(dir));
		return decodedDirs;
	}
	
	public static List<String> encodeDirs(List<String> dirs) {
		if(dirs == null)
			return null;
		List<String> encodedDirs = new Vector<String>(dirs.size());
		for(String dir: dirs)
			encodedDirs.add(encodeDir(dir));
		return encodedDirs;
	}

	public static void encodeDir(JsTreeFile file) {
		file.setEncPath(encodeDir(file.getPath()));
		file.setParentsEncPathes(getParentsEncPathes(file));
		encodeDir(file.getChildren());
	}

	public static void encodeDir(List<JsTreeFile> files) {
		if(files!=null)
			for(JsTreeFile file: files)
				encodeDir(file);
	}
	
	public static SortedMap<String, List<String>> getParentsPathes(JsTreeFile file) {
		return getParentsPathes(file.getPath(), file.getCategoryIcon(), file.getDriveIcon());
	}
	
	// Map<path, List<title, icon>>
	public static SortedMap<String, List<String>> getParentsPathes(String path, String categoryIcon, String driveIcon) {
		SortedMap<String, List<String>> parentsPathes = new TreeMap<String, List<String>>();
		String pathBase = JsTreeFile.ROOT_DRIVE;
		List<String> rootTitleIcon =  Arrays.asList(JsTreeFile.ROOT_DRIVE_NAME, JsTreeFile.ROOT_ICON_PATH);
		parentsPathes.put(pathBase, rootTitleIcon);
		String regexp = "(/|".concat(JsTreeFile.DRIVE_PATH_SEPARATOR).concat(")");
		String driveRootPath = path.substring(pathBase.length());
		if(driveRootPath.length() != 0) {
			List<String> relParentsPathes = Arrays.asList(driveRootPath.split(regexp));
			pathBase = pathBase.concat(relParentsPathes.get(0));
			List<String> categoryTitleIcon =  Arrays.asList(relParentsPathes.get(0), categoryIcon);
			parentsPathes.put(pathBase, categoryTitleIcon);
			if(relParentsPathes.size() > 1) {
				pathBase = pathBase.concat(JsTreeFile.DRIVE_PATH_SEPARATOR).concat(relParentsPathes.get(1));
				List<String> driveTitleIcon =  Arrays.asList(relParentsPathes.get(1), driveIcon);
				parentsPathes.put(pathBase, driveTitleIcon);
				pathBase = pathBase.concat(JsTreeFile.DRIVE_PATH_SEPARATOR);
				for(String parentPath: relParentsPathes.subList(2, relParentsPathes.size())) {
					pathBase = pathBase.concat(parentPath);
					List<String> folderTitleIds = Arrays.asList(parentPath.split(JsTreeFile.ID_TITLE_SPLIT));
					String title = folderTitleIds.get(folderTitleIds.size()-1);
					List<String> folderTitleIcon =  Arrays.asList(title, JsTreeFile.FOLDER_ICON_PATH);
					if(driveRootPath.endsWith("/"))
						pathBase = pathBase.concat("/");
					parentsPathes.put(pathBase, folderTitleIcon);
					if(!driveRootPath.endsWith("/"))
						pathBase = pathBase.concat("/");
				}
			}
		}
		return parentsPathes;
	}

	public static SortedMap<String, List<String>> getParentsEncPathes(JsTreeFile file) {
		return getParentsEncPathes(file.getPath(), file.getCategoryIcon(), file.getDriveIcon());
	}
	
	// Map<path, List<title, icon>>                                                                                                                     
	public static SortedMap<String, List<String>> getParentsEncPathes(String path, String categoryIcon, String driveIcon) {
		SortedMap<String, List<String>> parentPathes = getParentsPathes(path, categoryIcon, driveIcon);
		SortedMap<String, List<String>> encodedParentPathes = new  TreeMap<String, List<String>>();
		for(String pathKey : parentPathes.keySet()) {
			String encodedPath = encodeDir(pathKey);
			encodedParentPathes.put(encodedPath, parentPathes.get(pathKey));
		}
		return encodedParentPathes;
	}
}