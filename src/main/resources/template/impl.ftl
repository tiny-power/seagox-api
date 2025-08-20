package com.seagox.lowcode.${mark}.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.seagox.lowcode.${mark}.service.I${tableUpper}Service;
import com.seagox.lowcode.${mark}.mapper.${tableUpper}Mapper;

/**
* ${tableComment}
*/
@Service
public class ${tableUpper}Service implements I${tableUpper}Service {
	
	@Autowired
    private ${tableUpper}Mapper ${tableLower}Mapper;
    
}