package com.example.wp.resource.basic;

public class BasicConst {
	
	public static final String CACHE_DIRECTORY_NAME = "jln";
	/** 验证码超时时间 */
	public static final long VERIFICATION_COUNT_DOWN_TIME = 60000;
	/** 验证码时间区间 */
	public static final long VERIFICATION_COUNT_DOWN_INTERVAL = 1000;
	/** 验证码长度 */
	public static final int VERIFICATION_CODE_LENGTH = 4;
	/** 支付密码长度 */
	public static final int PAYMENT_PASSWORD_LENGTH = 6;
	/** 数值格式化 */
	public static final String NUMBER_FORMAT = "#,###,##0.00";
	/** 金额格式化 */
	public static final String MONEY_FORMAT = "#,###,##0.00";
	/** 优惠券金额格式 */
	public static final String COUPON_MONEY_FORMAT = "######0";
	/** 图片最大宽度 */
	public static final int MAX_PICKER_PICTURE_WIDTH = 1080;
	/** 图片最大高度 */
	public static final int MAX_PICKER_PICTURE_HEIGHT = 0;
	/** 图片压缩质量 */
	public static final int PICKER_PICTURE_COMPRESS_QUALITY = 80;
	/** 图片显示比例 */
	public static final float PICTURE_RATIO = 0.75F;
	/** 网页格式 */
	public static final String WEB_MIME_TYPE = "text/html; charset=UTF-8";
	/** 向量KEY */
	public static final String IV_KEY = "zhuh#juniuu@2016";
	/** 加密KEY */
	public static final String SECRET_KEY = "zhuzhjuniu@k2016";
	
	public static final int REQUEST_CODE_LOGIN = 999;
	
	/**
	 * 调整Web内容宽度
	 *
	 * @param content Web内容
	 * @return 调整结果
	 */
	public static String wrapWebContent(String content) {
		if (content == null || "null".equalsIgnoreCase(content)) {
			return "";
		}
		return "<style>img{display: inline;height: auto;max-width: 100%;}*{word-break:break-word;}</style>" + content;
	}
}
