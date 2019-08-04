package com.mogoo.market.network;

/**
 * 网络相关业务
 * 
 * @author 张永辉
 * @date 2011-10-25
 */
public class NetworkManager {
	/**
	 * GPRS网络
	 */
	public static final int NETWORK_STATUS_GPRS = 0;
	/**
	 * wifi网络
	 */
	public static final int NETWORK_STATUS_WIFI = 1;
	/**
	 * 网络不可用
	 */
	public static final int NETWORK_STATUS_NOT_AVAILABLE = -1;
	/**
	 * 服务器连接成功
	 */
	public static final int SERVER_CONNECT_STATUS_SUCCESS = 0;
	/**
	 * 服务器连接失败
	 */
	public static final int SERVER_CONNECT_STATUS_FAIL = -1;

	/**
	 * 网络连接状态
	 */
	private int networkStatus;
	/**
	 * 服务器连接状态
	 */
	private int serverConnectStatus;

	private static NetworkManager instance;

	private NetworkManager() {
	}

	/**
	 * 取得实例
	 * 
	 * @author 张永辉
	 * @date 2011-10-25
	 * @return
	 */
	public static NetworkManager getInstance() {
		if (instance == null) {
			instance = new NetworkManager();
		}
		return instance;
	}

	public int getNetworkStatus() {
		return networkStatus;
	}

	public void setNetworkStatus(int networkStatus) {
		this.networkStatus = networkStatus;
	}

	public int getServerConnectStatus() {
		return serverConnectStatus;
	}

	public void setServerConnectStatus(int serverConnectStatus) {
		this.serverConnectStatus = serverConnectStatus;
	}

	/**
	 * 网络是否可用
	 * 
	 * @author 张永辉
	 * @date 2011-10-25
	 * @return
	 */
	public boolean isNetworkActive() {
		if (networkStatus == NETWORK_STATUS_GPRS
				|| networkStatus == NETWORK_STATUS_WIFI) {
			return true;
		} else if (networkStatus == NETWORK_STATUS_NOT_AVAILABLE) {
			return false;
		}
		return false;
	}

	/**
	 * 服务器连接是否可用
	 * 
	 * @author 张永辉
	 * @date 2011-10-25
	 * @return
	 */
	public boolean isServerConnectActive() {
		if (serverConnectStatus == SERVER_CONNECT_STATUS_SUCCESS) {
			return true;
		} else if (serverConnectStatus == SERVER_CONNECT_STATUS_FAIL) {
			return false;
		}
		return false;
	}
}
