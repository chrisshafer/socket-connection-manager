package com.cgs.enhancedsocket;

import java.math.BigInteger;
import java.security.SecureRandom;

public final class UniqueIdentifierGenerator
{


  public static String generateUniqueKey(int bits)
  {
	  SecureRandom random = new SecureRandom();
	  return new BigInteger(bits, random).toString(32);
  }

}