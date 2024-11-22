package com.ahold.ctp.assignment.util

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


fun logger(clazz: Class<Any>): Logger = LogManager.getLogger(clazz)