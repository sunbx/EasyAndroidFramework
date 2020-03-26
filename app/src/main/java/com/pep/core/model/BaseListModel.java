package com.pep.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sunbaixin QQ:283122529
 * @name pepCore
 * @class name：com.pep.core
 * @class describe
 * @time 2019-09-09 14:46
 * @change
 * @chang time
 * @class describe
 */
public class BaseListModel<T> {

    /**
     * code : 200
     * message : 成功!
     * result : [{}]
     */

    public int code;
    public String message;
    public ArrayList<T> result;

}
