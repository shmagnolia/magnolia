package com.magnolia.common;

import org.apache.velocity.VelocityContext;


public class VelocityContextUtils {

	public static VelocityContext copy(VelocityContext velocityContext){
		VelocityContext _velocityContext = new VelocityContext();
		if(velocityContext.getKeys() != null && velocityContext.getKeys().length > 0){
			for(Object key:velocityContext.getKeys()){
				_velocityContext.put(key.toString(), velocityContext.get(key.toString()));
			}
		}
		return _velocityContext;
	}

}
