package com.asksunny.tools;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public class PackageCopyTool {

	public PackageCopyTool() {
	}

	public static void copyDirectoryFile(File srcDir, String srcPkgName, File destDir, String destPkgName) {

		FileCopyFilter filter = new FileCopyFilter(srcPkgName, destDir, destPkgName);
		File[] dirs = srcDir.listFiles(filter);
		if (dirs != null && dirs.length > 0) {
			for (File dir : dirs) {
				String name = dir.getName();
				File ndDir = new File(destDir, name);
				if (!ndDir.exists() && !ndDir.mkdirs()) {
					throw new RuntimeException("Failed to create destination directory:" + ndDir.toString());
				}
				copyDirectoryFile(dir, srcPkgName, ndDir, destPkgName);
			}
		}
	}

	private static class FileCopyFilter implements FileFilter {

		String srcPkgName;
		String srcPkgPath;
		int srcPkgLength = 0;
		File destDir;
		String destPkgName;
		String destPkgPath;
		public FileCopyFilter(String srcPkgName, File destDir, String destPkgName) {
			super();
			this.srcPkgName = srcPkgName;
			this.destDir = destDir;
			this.destPkgName = destPkgName;
			this.srcPkgPath = srcPkgName.replaceAll("[.]", "/");
			this.destPkgPath = destPkgName.replaceAll("[.]", "/");
			srcPkgLength = this.srcPkgPath.length();
			String dppath = destDir.toString().replaceAll("[\\\\]", "/");
			System.out.println(">>>>>>>>>>>>>>>>" + removeIntersect(destDir, destPkgName));
			
			System.out.println(destPkgPath);
			System.out.println(dppath);
		}
		
		
		public File removeIntersect(File base, String pkgName)
		{
			String dppath = base.toString().replaceAll("[\\\\]", "/");
			String[]  pkgs = pkgName.split("[.]");
			for (int i = 0; i < pkgs.length; i++) {
				String[] ap = new String[i+1];
				System.arraycopy(pkgs, 0, ap, 0, i+1);
				String rpath = StringUtils.join(ap, "/");
				if(dppath.endsWith(rpath)){
					String tpath = dppath.substring(0, dppath.length()-rpath.length());					
					
					return new File(tpath);					
				}
			}
			return base;
		}
		
		

		@Override
		public boolean accept(File pathname) {
			if (pathname.isDirectory()) {
				return true;
			} else {
				try {
					String ppath = pathname.getParent().replaceAll("[\\\\]", "/");
					//System.out.println(ppath);
					String fn = pathname.getName();					
					String sourceCode = IOUtils.toString(pathname.toURI(), "UTF-8");
					String updated = sourceCode.replaceAll(Pattern.quote(srcPkgName), destPkgName);
					if (!destDir.exists() && !destDir.mkdirs()) {
						throw new RuntimeException("Failed to create destination directory:" + destDir.toString());
					}
					FileOutputStream fout = new FileOutputStream(new File(destDir, fn));
					try {
						IOUtils.write(updated, fout, "UTF-8");
						fout.flush();
					} finally {
						fout.close();
					}
				} catch (IOException e) {
					throw new RuntimeException("Failed to copy file", e);
				}

			}
			return false;
		}

	}

	public static void main(String[] args) throws Exception {

		PackageCopyTool.copyDirectoryFile(new File("C:/Users/SunnyLiu/git/jscaffold/src/main/java/com/asksunny"),
				"com.asksunny", new File("C:/Users/SunnyLiu/git/jscaffold/target/src/com/xyz/data"), "com.xyz.data");
	}

}
