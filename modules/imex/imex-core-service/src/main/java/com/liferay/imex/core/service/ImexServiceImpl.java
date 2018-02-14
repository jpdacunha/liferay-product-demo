package com.liferay.imex.core.service;

import com.liferay.imex.core.api.ImexService;
import com.liferay.imex.core.api.archiver.ImexArchiverService;
import com.liferay.imex.core.api.configuration.ImexConfigurationService;
import com.liferay.imex.core.api.identifier.ProcessIdentifier;
import com.liferay.imex.core.service.configuration.model.ConfigurationProcessIdentifier;
import com.liferay.imex.core.util.statics.MessageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;

@Component(
		immediate = true,
		service = ImexService.class
	)
public class ImexServiceImpl extends ImexServiceBaseImpl implements ImexService {
	
	private static final Log _log = LogFactoryUtil.getLog(ImexServiceImpl.class);

	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexArchiverService imexArchiverService;
	
	@Reference(cardinality=ReferenceCardinality.MANDATORY)
	protected ImexConfigurationService configurationService;
	
	@Override
	public void generateOverrideFileSystemConfigurationFiles() {
		generateOverrideFileSystemConfigurationFiles(null, true);
	}
	
	@Override
	public void generateOverrideFileSystemConfigurationFiles(List<String> bundleNames, boolean archive) {
				
		Map<String,Properties> props = configurationService.loadAllConfigurationMap(bundleNames);
		
		ProcessIdentifier processIdentifier = new ConfigurationProcessIdentifier();
		
		//Initialisation répertoire de configuration
		initializeConfigurationtDirectory();
		
		if (props != null) {
			
			if (archive) {
				
				Properties coreConfig = configurationService.loadCoreConfiguration();
				imexArchiverService.archiveCfg(coreConfig, processIdentifier);
				
			}
			
			for (Map.Entry<String ,Properties> entry  : props.entrySet()) {
				
				Properties properties = entry.getValue();
				
				if (properties!= null && !properties.isEmpty()) {
					
					File propsFile = configurationService.getConfigurationOverrideFileName(entry);
					
					if (!propsFile.exists()) {
						
						FileOutputStream stream = null;
						try {
							
							try {
								stream = new FileOutputStream(propsFile);
								properties.store(stream, "Generated by imex");
							} finally {
						        if (stream != null) stream.close();
						    }
							
						} catch (IOException e) {
							 _log.error(e,e);
					    }
						
					} else {
						_log.error("Keeping existing file");
					}
					
				} else {
					_log.warn(MessageUtil.getEmpty(entry.getKey()));
				}	
			
			}
			
		}
		
	}
	
	private File initializeConfigurationtDirectory() {
		
		String cfgFilePath = configurationService.getImexCfgOverridePath();
		
		File cfgFile = new File(cfgFilePath);
		
		cfgFile.mkdirs();
		if (!cfgFile.exists()) {
			_log.error("Failed to create directory " + cfgFile);
		}
		
		return cfgFile;
			
	}

}
