package com.shengfei.constant;

import com.shengfei.config.SpringContextUtils;

public class HouseUrlConstant {

    private static final String houseUrl = SpringContextUtils.getProperty("house.url");;

    public final static String HOUSE_LOGIN = houseUrl+"/Login";

    public final static String HOUSE_PROVINCES = houseUrl+"/Provinces";

    public final static String HOUSE_CITYS = houseUrl+"/Citys/{provinceId}";

    public final static String HOUSE_AREAS = houseUrl+"/Areas/{cityId}";

    public final static String HOUSE_CONSTRUCTION = houseUrl+"/GetConstruction/{cityId}/{buildName}";

    public final static String HOUSE_CONSTRUTIONVIEW = houseUrl+"/GetConstrutionViewInfoById/{constructId}";

    public final static String HOUSE_BUILD = houseUrl+"/GetBuild/{constructId}";

    public final static String HOUSE_HOUSE = houseUrl+"/GetHouse/{buildId}";

    public final static String HOUSE_ESTATEEVALUATION = houseUrl+"/EstateEvaluation";

    public final static String HOUSE_PRINTINFO = houseUrl+"/PrintInfo/{caseId}";



}
