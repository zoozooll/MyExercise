/**
 * 
 */
package com.btf.push;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Bind;
import org.jivesoftware.smack.packet.Packet;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.butterfly.vv.vv.utils.VVXMPPUtils;

/**
 * @author hongbo ke
 *
 */
public class BindPacketListener implements PacketListener {
	/* (non-Javadoc)
	 * @see org.jivesoftware.smack.PacketListener#processPacket(org.jivesoftware.smack.packet.Packet)
	 */
	@Override
	public void processPacket(Packet packet) {
		if (packet instanceof Bind) {
			Bind bind = (Bind) packet;
			LoginManager.getInstance().saveSessionId(
					bind.getVvsid());
			SharedPrefsUtil.putValue(BeemApplication.getContext(),
					SettingKey.account_username, VVXMPPUtils.makeJidParsed(bind.getJid()));
		}
	}
}
