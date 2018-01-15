package org.smart4j.plugin.security.tag;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.tags.PermissionTag;

import java.util.Arrays;

/**
 * 判断当前用户是否拥有其中所有的权限（逗号分隔，表示"与"的关系）
 * @author wangjianchun
 * @create 2018/1/15
 */
public class HasAllPermissionsTag extends PermissionTag {

    private static final String ROLE_NAMES_DEMILITER = ",";

    @Override
    protected boolean showTagBody(String permissions) {
        boolean hasAllPermission = false;
        Subject subject = getSubject();
        if(subject != null){
            boolean[] booleans = subject.isPermitted(permissions.split(ROLE_NAMES_DEMILITER));
            if(!Arrays.asList(booleans).contains(false)){
                hasAllPermission = true;
            }
        }
        return hasAllPermission;
    }
}
