package com.butterfly.vv.upload;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerPNames;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.impl.cookie.BrowserCompatSpec;
import org.apache.http.impl.cookie.CookieSpecBase;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.LogUtils;
import com.beem.project.btf.utils.PictureUtil;
import com.butterfly.vv.upload.CustomMultiPartEntity.ProgressListener;
import com.butterfly.vv.upload.NetParamter.NetParamterType;
import com.butterfly.vv.vv.utils.Debug;
import com.butterfly.vv.vv.utils.JsonParseUtils;

public class AppachUpload {
	private static final Charset UTF_8 = Charset.forName("UTF-8");
	private static final String USER_AGENT = "User-Agent";
	private final DefaultHttpClient defaultHttpClient;
	private final CookieSpecBase cookieSpecBase = new BrowserCompatSpec();
	private final List<Cookie> sessionCookies = new ArrayList<Cookie>();
	private final BasicHeader basicHeader;
	private String lat, lon;
	private String address;
	private MultipartEntity multiPartEntity;
	private static String prefix = org.jivesoftware.smack.util.StringUtils
			.randomString(5);
	private static long id = 0;

	private static synchronized String nextID() {
		return prefix + Long.toString(id++);
	}
	public AppachUpload(String userAgent) {
		// TODO Auto-generated constructor stub
		this.basicHeader = new BasicHeader(USER_AGENT, userAgent);
		sessionCookies.add(new BasicClientCookie("", ""));
		// the httpclient initialization is heavy, we create one for
		// G2ConnectionUtils
		defaultHttpClient = createHttpClient();
	}
	public Map<String, Object> sendImageToGallery2(String galleryUrl,
			int albumName, File imageFile, String imageName, String summary,
			String description, String path, String jid, String groupid,
			String info) throws Exception {
		MultipartEntity multiPartEntity = createMultiPartEntityForSendImageToGallery2(
				albumName, imageFile, imageName, summary, description, path,
				jid, groupid, info);
		Map<String, Object> properties = sendCommandToGallery(galleryUrl, null,
				multiPartEntity);
		if ("200".equals(properties.get("status"))) {
			Debug.getDebugInstance().log("upload successfully");
		}
		return properties;
	}
	public Map<String, Object> sendImageToGallery5(String galleryUrl,
			int albumName, File imageFile, String imageName, String summary,
			String description, List<Uri> urilist, String usepath, String jid,
			String info) throws Exception {
		MultipartEntity multiPartEntity = createMultiPartEntityForSendImageToGallery5(
				albumName, imageFile, imageName, summary, description, urilist,
				usepath, jid, info);
		Map<String, Object> properties = sendCommandToGallery(galleryUrl, null,
				multiPartEntity);
		Log.i("gemei", "status===" + "200".equals(properties.get("status")));
		if ("200".equals(properties.get("status"))) {
			Debug.getDebugInstance().log("upload successfully");
		}
		return properties;
	}
	public int sendPostImageToGallery2(String galleryUrl, int albumName,
			File imageFile, String imageName, String summary,
			String description, String subject, String content, String uid,
			String cid, String info, List<Uri> urilist) throws Exception {
		int response = 0;
		MultipartEntity multiPartEntity = createPostMultiPartEntityForSendImageToGallery2(
				albumName, imageFile, imageName, summary, description, subject,
				content, uid, cid, info, urilist);
		Map<String, Object> properties = sendCommandToGallery(galleryUrl, null,
				multiPartEntity);
		if ("200".equals(properties.get("status"))) {
			response = 200;
			Debug.getDebugInstance().log("upload successfully");
		}
		return response;
	}
	@SuppressWarnings("deprecation")
	public synchronized Map<String, Object> sendPostImageToGallery2(
			String urlPrefix, NetParamter[] pareTypeList)
			throws UnsupportedEncodingException, IllegalArgumentException,
			FileNotFoundException, Exception {
		long lastMills = System.currentTimeMillis();
		Bitmap bmpCompressed = null;
		Bitmap bmpCompressed_big = null;
		MultipartEntity multiPartEntity = new MultipartEntity();
		Map<String, File> tempFiles = new HashMap<String, File>();
		try {
			for (NetParamter npType : pareTypeList) {
				if (npType.getNetParamterType() == NetParamterType.type_file) {
					File orginalFile = new File(npType.getValue());
					bmpCompressed_big = PictureUtil.revitionImage(orginalFile
							.getPath());
					File file = saveImage(bmpCompressed_big);
					if (file != null) {
						tempFiles.put("file_big", file);
						multiPartEntity.addPart(npType.getName(), new FileBody(
								file));
					} else {
						multiPartEntity.addPart(
								npType.getName(),
								new ByteArrayBody(PictureUtil
										.getBytes(bmpCompressed_big),
										ContentType.create("image/*"),
										orginalFile.getName()));
					}
					if (bmpCompressed_big != null
							&& !bmpCompressed_big.isRecycled()) {
						//LogUtils.i("Recycle bmpCompressed_big");
						bmpCompressed_big.recycle();
						bmpCompressed_big = null;
					}
					if (npType.isCreateThumb()) {
						bmpCompressed = PictureUtil.revitionImage(
								orginalFile.getPath(), 280, 280);
						File small_file = saveImage(bmpCompressed);
						if (small_file != null) {
							tempFiles.put("file_small", small_file);
							multiPartEntity.addPart(npType.getName(),
									new FileBody(small_file));
						} else {
							String[] splits = orginalFile.getName()
									.split("[.]");
							String thumbName = splits[0] + "_thumb."
									+ splits[1];
							multiPartEntity.addPart(
									npType.getName(),
									new ByteArrayBody(PictureUtil
											.getBytes(bmpCompressed),
											ContentType.create("image/*"),
											thumbName));
						}
					}
					if (bmpCompressed != null && !bmpCompressed.isRecycled()) {
						//LogUtils.i("Recycle bmpCompressed");
						bmpCompressed.recycle();
						bmpCompressed = null;
					}
				} else if (npType.getNetParamterType() == NetParamterType.type_str) {
					multiPartEntity.addPart(npType.getName(), new StringBody(
							npType.getValue(), UTF_8));
				} else {
					throw new IllegalArgumentException(
							"~Error:~npType's  getNetParamterType() is wrong~"
									+ npType.getNetParamterType());
				}
			}
			LogUtils.i(urlPrefix + " request==>" + multiPartEntity);
			Map<String, Object> properties = sendCommandToGallery(urlPrefix,
					null, multiPartEntity);
			LogUtils.i(urlPrefix + " response==>" + (properties)
					+ " costMills:" + (System.currentTimeMillis() - lastMills));
			return properties;
		} finally {
			//delete the temp files
			for (String key : tempFiles.keySet()) {
				File file = tempFiles.get(key);
				file.delete();
				//LogUtils.v("delete the tempfile:[" + key + "," + file.getPath() + "]");
			}
			tempFiles.clear();
		}
	}
	/***
	 * 保存上传图片
	 */
	private File saveImage(Bitmap bitmap) {
		File file = null;
		if (!android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			return file;
		}
		String newFileName = StringUtils.join(new Object[] { "tmp",
				System.currentTimeMillis(), nextID(), ".jpg" });
		File updateFile = new File(BBSUtils.getAppCacheDir(
				BeemApplication.getContext(), "uploadTempFile"), newFileName);
		// now save out the file holmes!
		OutputStream outStream = null;
		if (updateFile != null) {
			try {
				outStream = new FileOutputStream(updateFile);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outStream);
				try {
					outStream.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
			}
		}
		return updateFile;
	}
	/**
	 * @param urlPrefix
	 * @param pareTypeList
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 * @throws Exception
	 */
	@SuppressWarnings({ "null", "deprecation" })
	public List<Map<String, Object>> SendPostImageToGallerySet(
			String urlPrefix, List<NetParamter[]> pareTypeList)
			throws UnsupportedEncodingException, IllegalArgumentException,
			FileNotFoundException, Exception {
		List<Map<String, Object>> propertieslist = null;
		MultipartEntity multiPartEntity = new MultipartEntity();
		for (int i = 0; i < pareTypeList.size(); i++) {
			for (NetParamter npType : pareTypeList.get(i)) {
				if (npType.getNetParamterType() == NetParamterType.type_file) {
					File orginalFile = new File(npType.getValue());
					multiPartEntity.addPart(npType.getName(), new FileBody(
							orginalFile, ContentType.create("image/*"),
							orginalFile.getName()));
					if (npType.isCreateThumb()) {
						Bitmap bitmap = BitmapFactory.decodeFile(npType
								.getValue());
						Bitmap bmpCompressed = Bitmap.createScaledBitmap(
								bitmap, 64, 48, true);
						ByteArrayOutputStream os = new ByteArrayOutputStream();
						bmpCompressed.compress(CompressFormat.JPEG, 100, os);
						String[] splits = orginalFile.getName().split("[.]");
						String thumbName = splits[0] + "_thumb." + splits[1];
						multiPartEntity.addPart(
								npType.getName(),
								new ByteArrayBody(os.toByteArray(), ContentType
										.create("image/*"), thumbName));
					}
				} else if (npType.getNetParamterType() == NetParamterType.type_str) {
					multiPartEntity.addPart(npType.getName(), new StringBody(
							npType.getValue(), UTF_8));
				} else {
					throw new IllegalArgumentException(
							"~Error:~npType's  getNetParamterType() is wrong~"
									+ npType.getNetParamterType());
				}
			}
			Map<String, Object> properties = sendCommandToGallery(urlPrefix,
					null, multiPartEntity);
			propertieslist.add(properties);
		}
		return propertieslist;
	}
	public int sendPostImageToGallery2x(String galleryUrl, String[] names,
			Object[] values) throws Exception {
		//LogUtils.i("~~~~~~galleryUrl~~~~~~" + galleryUrl + "~~~names~~~~~" + Arrays.toString(names) + "~~~values~~~~~"
		//				+ Arrays.toString(values));
		int response = 0;
		MultipartEntity multiPartEntity = createPostMultiPartEntityForSendImageToGallery2x(
				names, values);
		Map<String, Object> properties = sendCommandToGallery(galleryUrl, null,
				multiPartEntity);
		if ("200".equals(properties.get("status"))) {
			response = 200;
			Debug.getDebugInstance().log("upload successfully");
		}
		return response;
	}
	// bbs add start
	// ///////////////////////////////////////////////
	// �������ݻظ�
	private MultipartEntity createPostMultiPartEntityForSendImageToGallery2(
			int albumName, File imageFile, String imageName, String summary,
			String description, String subject, String content, String uid,
			String cid, String info, List<Uri> urilist) throws Exception {
		try {
			multiPartEntity = new MultipartEntity();
			multiPartEntity.addPart("a", new StringBody("posts", UTF_8));
			multiPartEntity.addPart("subject", new StringBody(subject, UTF_8));
			multiPartEntity.addPart("content", new StringBody(content, UTF_8));
			multiPartEntity.addPart("uid", new StringBody(uid, UTF_8));
			multiPartEntity.addPart("cid", new StringBody(cid, UTF_8));
			System.out.println("imageFile:" + imageFile + " -content- "
					+ content + " -uid- " + uid + " -cid- " + cid + " -info- "
					+ info);
			System.out.println("urilist==========" + urilist.size());
			for (int i = 0; i < urilist.size(); i++) {
				System.out.println("imageFile:" + imageFile + " -content- "
						+ content + "<<<<" + imageName + " -jid- "
						+ urilist.get(i).getPath() + ">>uid>" + uid + " -cid- "
						+ cid + " -info- " + info);
				if (imageName == null && imageFile != null) {
					imageName = imageFile.getName().substring(0,
							imageFile.getName().indexOf("."));
				}
				String filepath = urilist.get(i).getPath();
				File file = new File(filepath);
				ContentBody cbFile = new FileBody(file, "image/png");
				multiPartEntity.addPart("data" + i, cbFile);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return multiPartEntity;
	}
	private MultipartEntity createPostMultiPartEntityForSendImageToGallery2x(
			String[] names, Object[] values) throws Exception {
		// MultipartEntity multiPartEntity;
		try {
			multiPartEntity = new MultipartEntity();
			for (int i = 0; i < names.length - 1; i++) {
				multiPartEntity.addPart(names[i],
						new StringBody(String.valueOf(values[i]), UTF_8));
			}
			String attatchKey = names[names.length - 1];
			List<Uri> urilist = (List<Uri>) values[values.length - 1];
			for (int i = 0; i < urilist.size(); i++) {
				String filepath = urilist.get(i).getPath();
				File file = new File(filepath);
				ContentBody cbFile = new FileBody(file, "image/png");
				multiPartEntity.addPart(attatchKey + i, cbFile);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return multiPartEntity;
	}
	public int sendPostImageToGallery3(String galleryUrl, int albumName,
			File imageFile, String imageName, String summary,
			String description, String path, String jid, String content,
			String info, String smilies, String author, String icon,
			List<Uri> urilist) throws Exception {
		int imageCreatedName = 0;
		int response = 0;
		// MultipartEntity multiPartEntity =
		// createPostreplyidMultiPartEntityForSendImageToGallery2(
		// albumName, imageFile, imageName, summary, description, path,
		// jid, groupid, info, urilist);
		MultipartEntity multiPartEntity = createPostreplyidMultiPartEntityForSendImageToGallery2(
				albumName, imageFile, imageName, summary, description, path,
				jid, content, smilies, author, icon, info, urilist);
		Map<String, Object> properties = sendCommandToGallery(galleryUrl, null,
				multiPartEntity);
		if ("200".equals(properties.get("status"))) {
			response = 200;
			Debug.getDebugInstance().log("upload successfully");
		}
		return response;
	}
	// ���ݻظ�
	public MultipartEntity createPostreplyidMultiPartEntityForSendImageToGallery2(
			int albumName, File imageFile, String imageName, String summary,
			String description, String path, String postsid, String content,
			String smilies, String author, String icon, String info,
			List<Uri> urilist) throws Exception {
		try {
			multiPartEntity = new MultipartEntity();
			multiPartEntity.addPart("a", new StringBody("reply", UTF_8));
			multiPartEntity.addPart("uid", new StringBody(path, UTF_8));
			multiPartEntity.addPart("postsid", new StringBody(postsid, UTF_8));
			multiPartEntity.addPart("content", new StringBody(content, UTF_8));
			multiPartEntity.addPart("smilies", new StringBody(smilies, UTF_8));
			multiPartEntity.addPart("author", new StringBody(author, UTF_8));
			multiPartEntity.addPart("icon", new StringBody(icon, UTF_8));
			for (int i = 0; i < urilist.size(); i++) {
				Log.i("cenimei", "path=" + path + ";   postsid=" + postsid
						+ ";   content=" + content + ";   ");
				if (imageName == null && imageFile != null) {
					imageName = imageFile.getName().substring(0,
							imageFile.getName().indexOf("."));
				}
				String filepath = urilist.get(i).getPath();
				// String filepath = imageName;
				File file = new File(filepath);
				ContentBody cbFile = new FileBody(file, "image/png");
				multiPartEntity.addPart("data" + i, cbFile);
			}
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return multiPartEntity;
	}
	// bbs add end
	private MultipartEntity createMultiPartEntityForSendImageToGallery5(
			int albumName, File imageFile, String imageName, String summary,
			String description, List<Uri> listuri, String usepath, String jid,
			String info) throws Exception {
		if (imageName == null && imageFile != null) {
			imageName = imageFile.getName().substring(0,
					imageFile.getName().indexOf("."));
		}
		// MultipartEntity multiPartEntity;
		try {
			multiPartEntity = new MultipartEntity();
			multiPartEntity.addPart("a", new StringBody("upload", UTF_8));
			multiPartEntity.addPart("path", new StringBody(usepath, UTF_8));
			multiPartEntity.addPart("uid", new StringBody(jid, UTF_8));
			if (listuri != null)
				multiPartEntity.addPart("group",
						new StringBody(listuri.toString(), UTF_8));
			if (info != null)
				multiPartEntity.addPart("status", new StringBody(info, UTF_8));
			// multiPartEntity.addPart("lon", new StringBody(lon, UTF_8));
			// if (address == null)
			// address = "δ֪";
			// multiPartEntity.addPart("address", new StringBody(address,
			// UTF_8));
			String filepath = imageName;
			File file = new File(filepath);
			ContentBody cbFile = new FileBody(file, "image/png");
			multiPartEntity.addPart("data", cbFile);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return multiPartEntity;
	}
	// //////////////////////////////////////////////
	private MultipartEntity createMultiPartEntityForSendImageToGallery(
			int albumName, File imageFile, String imageName, String summary,
			String description) throws Exception {
		if (imageName == null && imageFile != null) {
			imageName = imageFile.getName().substring(0,
					imageFile.getName().indexOf("."));
		}
		try {
			multiPartEntity = new MultipartEntity();
			multiPartEntity.addPart("a", new StringBody("upload", UTF_8));
			multiPartEntity.addPart("path", new StringBody("16", UTF_8));
			multiPartEntity.addPart("uid", new StringBody("" + 16, UTF_8));
			multiPartEntity.addPart("group", new StringBody("1", UTF_8));
			multiPartEntity.addPart("info", new StringBody("png", UTF_8));
			System.out.println("imageFile:" + imageName);
			// String filepath = "/mnt/sdcard/4.png";
			String filepath = imageName;
			File file = new File(filepath);
			ContentBody cbFile = new FileBody(file, "image/jpeg");
			multiPartEntity.addPart("data", cbFile);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return multiPartEntity;
	}
	private MultipartEntity createMultiPartEntityForSendImageToGallery2(
			int albumName, File imageFile, String imageName, String summary,
			String description, String path, String jid, String groupid,
			String info) throws Exception {
		if (imageName == null && imageFile != null) {
			imageName = imageFile.getName().substring(0,
					imageFile.getName().indexOf("."));
		}
		try {
			System.out.println("listener " + listener);
			multiPartEntity = new CustomMultiPartEntity(listener);
			Log.i("cenimei", "upload=====" + path + ";    jid=" + jid
					+ " ;    groupid =" + groupid + ";     info=" + info
					+ ";     lat=" + lat + ";    lon=" + lon + ";  address= "
					+ address);
			multiPartEntity.addPart("a", new StringBody("upload", UTF_8));
			multiPartEntity.addPart("path", new StringBody(path, UTF_8));
			multiPartEntity.addPart("uid", new StringBody(jid, UTF_8));
			if (groupid != null)
				multiPartEntity
						.addPart("group", new StringBody(groupid, UTF_8));
			if (info != null)
				multiPartEntity.addPart("status", new StringBody(info, UTF_8));
			System.out.println("======" + lat + ">>" + lon + address);
			Log.i("WWW", "==AppachUpload====" + lat + ">>" + lon + address);
			String tm_id = "1234567890";
			multiPartEntity.addPart("tm_id", new StringBody(tm_id, UTF_8));
			String gid = "20140625";
			multiPartEntity.addPart("gid", new StringBody(gid, UTF_8));
			String signature = "signature";
			multiPartEntity.addPart("signature", new StringBody(signature,
					UTF_8));
			String authority = "1";
			multiPartEntity.addPart("authority", new StringBody(authority,
					UTF_8));
			String action = "1";
			multiPartEntity.addPart("action", new StringBody(action, UTF_8));
			String createTime = "20140625";
			multiPartEntity.addPart("gid_create_time", new StringBody(
					createTime, UTF_8));
			String thumbUp = "0";
			multiPartEntity.addPart("thumbUp", new StringBody(thumbUp, UTF_8));
			String lat = "100";
			String lon = "100";
			if (lat != null)
				// lat = "22.71945";
				multiPartEntity.addPart("lat", new StringBody(lat, UTF_8));
			if (lon != null)
				// lon = "114.38657";
				multiPartEntity.addPart("lon", new StringBody(lon, UTF_8));
			if (address != null)
				// address = "δ֪";
				multiPartEntity.addPart("address", new StringBody(address,
						UTF_8));
			System.out.println("imageFile:" + imageName + " -path- " + path
					+ " -jid- " + jid + " -groupid- " + groupid + " -info- "
					+ info);
			// String filepath = "/mnt/sdcard/4.png
			String filepath = imageName;
			File file = new File(filepath);
			ContentBody cbFile = new FileBody(file, "image/*");
			multiPartEntity.addPart("data", cbFile);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return multiPartEntity;
	}
	private Map<String, Object> sendCommandToGallery(String galleryUrl,
			List<NameValuePair> nameValuePairsForThisCommand,
			HttpEntity multiPartEntity) throws Exception {
		Map<String, Object> properties = null;
		try {
			// retrieve previous cookies
			List<Cookie> cookies = defaultHttpClient.getCookieStore()
					.getCookies();
			// disable expect-continue handshake (lighttpd doesn't supportit)
			defaultHttpClient.getParams().setBooleanParameter(
					"http.protocol.expect-continue", false);
			defaultHttpClient.getParams().setParameter(
					CoreConnectionPNames.SOCKET_BUFFER_SIZE, 1 * 1024 * 1024);
			defaultHttpClient.getParams().setParameter(
					CoreConnectionPNames.TCP_NODELAY, true);
			// bug #25 : for embedded gallery, should not add main.php
			// galleryUrl = "http://192.168.0.202/web/app/share/index/login";
			String correctedGalleryUrl = galleryUrl;
			// if (!G2ConvertUtils.isEmbeddedGallery(galleryUrl)) {
			// correctedGalleryUrl = galleryUrl + "/" + MAIN_PHP;
			// }
			HttpPost httpPost = new HttpPost(correctedGalleryUrl);
			// if we send an image to the gallery, we pass it to the gallery
			// through multipartEntity
			httpPost.setHeader(basicHeader);
			// Setting the cookie
			httpPost.setHeader(getCookieHeader(cookieSpecBase));
			if (multiPartEntity != null) {
				((HttpEntityEnclosingRequestBase) httpPost)
						.setEntity(multiPartEntity);
			}
			HttpResponse response = null;
			// Execute HTTP Post Request and retrieve content
			try {
				response = defaultHttpClient.execute(httpPost);
			} catch (ClassCastException e) {
				response = defaultHttpClient.execute(httpPost);
			}
			int status = response.getStatusLine().getStatusCode();
			if (status >= HttpStatus.SC_BAD_REQUEST
					&& status <= HttpStatus.SC_INTERNAL_SERVER_ERROR) {
				//LogUtils.e("Error:" + response.getAllHeaders());
			}
			InputStream content = response.getEntity().getContent();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					content, "utf-8"), 4096);
			// do not forget the cookies
			sessionCookies.addAll(cookies);
			StringBuffer sb = new StringBuffer();
			String line = null;
			// boolean gr2ProtoStringWasFound = false;
			while ((line = rd.readLine()) != null) {
				sb.append(line);
			}
			LogUtils.i("rst:" + sb);
			properties = JsonParseUtils.parseToMap(sb.toString());
			content.close();
			rd.close();
		} catch (IOException e) {
			// something went wrong, let's throw the info to the UI
			throw new Exception(e);
		} catch (IllegalArgumentException e) {
			// the url is not correct
			throw new Exception(e);
		}
		return properties;
	}
	private Header getCookieHeader(CookieSpecBase cookieSpecBase) {
		List<Cookie> cookies = new ArrayList<Cookie>();
		cookies.addAll(sessionCookies);
		List<Header> cookieHeader = cookieSpecBase.formatCookies(cookies);
		return cookieHeader.get(0);
	}
	private DefaultHttpClient createHttpClient() {
		// avoid instanciating
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		// http scheme
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
				.getSocketFactory(), 80));
		// https scheme
		// schemeRegistry.register(new Scheme("https", new FakeSocketFactory(),
		// 443));
		HttpParams params = new BasicHttpParams();
		params.setParameter(ConnManagerPNames.MAX_TOTAL_CONNECTIONS, 30);
		params.setParameter(ConnManagerPNames.MAX_CONNECTIONS_PER_ROUTE,
				new ConnPerRouteBean(30));
		params.setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params,
				schemeRegistry);
		return new DefaultHttpClient(cm, params);
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	ProgressListener listener;

	public void setListener(ProgressListener listener) {
		this.listener = listener;
	}
	public long getFileSize() {
		return multiPartEntity.getContentLength();
	}
}
