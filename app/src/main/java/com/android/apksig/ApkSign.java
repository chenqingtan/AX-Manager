package com.android.apksig;


import android.content.*;
import android.util.*;
import com.android.apksig.ApkSigner.*;
import com.android.apksig.apk.*;
import com.google.common.collect.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;
import sun1.security.provider.*;

public class ApkSign
 {
  private static MessageDigest md5;
  private static MessageDigest sha1;
  private static MessageDigest sha256;
  public static Context Base;
  private X509Certificate certificate;
  private PrivateKey privateKey;
  public static boolean Verifier ( File in )
   {
	try
	 {
	  ApkVerifier.Builder b=new ApkVerifier.Builder ( in );
	  b.setMinCheckedPlatformVersion ( 14 );
	  b.setMaxCheckedPlatformVersion ( 27 );
	  ApkVerifier c= b.build ( );
	  ApkVerifier.Result r=c.verify ( );
	  if ( r.isVerified ( ) )
	   return true;
	  else
	   return false;
	 }
	catch (Exception e)
	 {
	  System.out.println ( Log.getStackTraceString ( e ) );
	  return false;
	 }
   }
  public static interface LoadKeyCallback
   {
	void call ( ApkSign signUtil );
   }
  public static void loadKey ( Context context, String Jks, String Alias, String AliasPass, String Pass, LoadKeyCallback loadKeyCallback ) throws Exception
   {
	Base = context;
	loadKey ( context, loadKeyCallback, new FileInputStream(Jks), Alias, Pass, AliasPass );
   }

  static {
	try
	 {
	  sha256 = MessageDigest.getInstance ( "SHA-256" );
	  sha1 = MessageDigest.getInstance ( "SHA-1" );
	  md5 = MessageDigest.getInstance ( "MD5" );
	 }
	catch (Exception e)
	 {
	 }
   }

  private static boolean loadKey ( Context context, LoadKeyCallback loadKeyCallback, InputStream inputStream, String str, String str2, String str3 ) throws Exception
   {
	char[] cArr;
	KeyStore instance = KeyStore.getInstance ( "JKS" );
	char[] toCharArray = str2.toCharArray ( );
	if ( str3.isEmpty ( ) )
	 {
	  cArr = toCharArray;
	 }
	else
	 {
	  cArr = str3.toCharArray ( );
	 }
	loadKey ( loadKeyCallback, instance, inputStream, str, toCharArray, cArr );
	return true;
   }

  private static void loadKey ( LoadKeyCallback loadKeyCallback, KeyStore keyStore, InputStream inputStream, String str, char[] cArr, char[] cArr2 ) throws Exception
   {
	keyStore.load ( inputStream, cArr );
	if ( str.isEmpty ( ) )
	 {
	  str = (String) keyStore.aliases ( ).nextElement ( );
	 }
	PrivateKey privateKey = (PrivateKey) keyStore.getKey ( str, cArr2 );
	X509Certificate x509Certificate = (X509Certificate) keyStore.getCertificate ( str );
	ApkSign signUtil = new ApkSign ( );
	signUtil.privateKey = privateKey;
	signUtil.certificate = x509Certificate;
	loadKeyCallback.call ( signUtil );
   }
  public void sign ( File file, File file2, int i ) throws SignatureException, IllegalStateException, NoSuchAlgorithmException, InvalidKeyException, IOException, ApkFormatException
   {
	Builder builder = new Builder ( ImmutableList.of ( new SignerConfig.Builder ( "_________", this.privateKey, ImmutableList.of ( this.certificate ) ).build ( ) ) );
	builder.setInputApk ( file );
	builder.setOutputApk ( file2 );
	builder.setCreatedBy ( "PanGolin" );
	builder.setMinSdkVersion ( i );
	builder.setV1SigningEnabled ( true );
	builder.setV2SigningEnabled ( true );
	builder.build ( ).sign ( );
	file.delete();
   }
 }

