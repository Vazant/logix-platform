package com.vazant.logix.orders.infrastructure.utils;

import org.jilt.Builder;
import org.jilt.BuilderStyle;

@Builder(style = BuilderStyle.STAGED_PRESERVING_ORDER, toBuilder = "copy")
public @interface JiltBuilder {}
