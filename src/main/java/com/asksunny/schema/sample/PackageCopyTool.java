package com.asksunny.schema.sample;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

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
		File destDir;
		String destPkgName;

		public FileCopyFilter(String srcPkgName, File destDir, String destPkgName) {
			super();
			this.srcPkgName = srcPkgName;
			this.destDir = destDir;
			this.destPkgName = destPkgName;
		}

		@Override
		public boolean accept(File pathname) {
			if (pathname.isDirectory()) {
				return true;
			} else {
				try {
					String fn = pathname.getName();
					String sourceCode = IOUtils.toString(pathname.toURI(), "UTF-8");
					String updated = sourceCode.replaceAll(Pattern.quote(srcPkgName), destPkgName);
					if (!destDir.exists() && !destDir.mkdirs()) {
						throw new RuntimeException("Failed to create destination directory:" + destDir.toString());
					}					
					FileOutputStream fout = new FileOutputStream(new File(destDir, fn));
					try {
						IOUtils.write(updated, fout, "UTF-8");
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

}
