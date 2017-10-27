package com.liferay.imex.core.api.exporter;

import java.io.File;
import java.util.Properties;

public interface Exporter {
	
	public void doExport(Properties config, File destDir, long companyId);
	
	public String getDirectoryName();
	
	public String getProcessDescription();
	
	public String getFileName();

}
