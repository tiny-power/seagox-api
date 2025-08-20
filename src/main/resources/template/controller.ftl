package com.seagox.lowcode.${mark}.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.seagox.lowcode.${mark}.service.I${tableUpper}Service;

/**
* ${tableComment}
*/
@RestController
@RequestMapping("/${tableLower}")
public class ${tableUpper}Controller {
	
	@Autowired
    private I${tableUpper}Service ${tableLower}Service;
    
}