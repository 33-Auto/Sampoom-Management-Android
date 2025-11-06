package com.sampoom.android.core.util

import com.sampoom.android.core.model.UserPosition

fun positionToKorean(position: UserPosition?): String = when (position) {
    UserPosition.STAFF -> "사원"
    UserPosition.SENIOR_STAFF -> "주임"
    UserPosition.ASSISTANT_MANAGER -> "대리"
    UserPosition.MANAGER -> "과장"
    UserPosition.DEPUTY_GENERAL_MANAGER -> "차장"
    UserPosition.GENERAL_MANAGER -> "부장"
    UserPosition.DIRECTOR -> "이사"
    UserPosition.VICE_PRESIDENT -> "부사장"
    UserPosition.PRESIDENT -> "사장"
    UserPosition.CHAIRMAN -> "회장"
    else -> "-"
}