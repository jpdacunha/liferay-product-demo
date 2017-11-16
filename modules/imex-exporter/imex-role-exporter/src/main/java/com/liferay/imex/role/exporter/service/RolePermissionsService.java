package com.liferay.imex.role.exporter.service;

import com.liferay.imex.role.exporter.xml.RolePermissions;

/**
 * 
 * @author jpdacunha
 * 
 *
 */
public interface RolePermissionsService {
	
	public RolePermissions getRolePermissions(
			long companyId, 
			long roleId)
		throws Exception;
	
}
