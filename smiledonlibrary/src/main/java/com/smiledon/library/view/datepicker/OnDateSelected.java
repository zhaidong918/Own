package com.smiledon.library.view.datepicker;

import java.util.List;

/**
 * The callback interface when date picked.
 * @author XZ
 */
public interface OnDateSelected {
	/**
     * The method when date picked will be called.
     * The format of string in list like yyyy-MM-dd.
     */
    void selected(List<String> date);
    
}
