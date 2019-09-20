package com.boot.meta;

import com.boot.domain.User;

import java.math.BigDecimal;

public class GlobalMeta {

    public static final String USER_KEY_PREFIX= "user_";

    public static final String REDPACK_KEY_PREFIX = "redpack_";

    public static final String REDPACK_LOG_PREFIX = "redpacklog_";

    public static final Integer REDPACK_MIN = 1;

    public static final Integer REDPACK_MAX = Integer.MAX_VALUE;

    public static final BigDecimal BASE_VALUE = BigDecimal.valueOf(100);

    public static final User NULL_USER = User.builder().id(null)
                                                        .address(null)
                                                        .age(null)
                                                        .name(null)
                                                        .score(null)
                                                        .gender(null).build();
}
