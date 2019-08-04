package com.oregonscientific.bbq.history;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import android.os.Environment;
import com.oregonscientific.bbq.bean.BBQDataSet;
import com.oregonscientific.bbq.bean.BbqRecord;
import com.oregonscientific.bbq.utils.BbqConfig;

public class WriteReadTempFile {
		private ArrayList<Float> allTemperature = new ArrayList<Float>(); 
		private File saveFile;
		private String filepath1;
	
		public BbqRecord PackRecord(int channel,BBQDataSet currentData,int count){
	
			BbqRecord record = new BbqRecord();
			BBQDataSet data = currentData;	
			
			record.setFinishedDate(System.currentTimeMillis());
			record.setChannel(channel);
			record.setMode(data.getMode());		
			record.setFinishedTemperature(data.getProbeTemperature());
			record.setCostTime(count);
			record.setCookingState(data.getStatus());
			//record.setGraphyPath(data.setGraphyPath());
			record.setTemperaturesFilepath(filepath1);
			//record.setMemo(data.getMemo());		
			record.setSetMeattype(data.getMeatTypeInt());
			record.setSetDoneness(data.getDonelessLevel());
			record.setSetTargeTemperature(data.getTargetTemperature());
			record.setSetTotalTime(data.getReloadTimer().totalSeconds());			

			record.setSetDonenessR(BbqConfig.MAP_MEATTYPE_INITIAL_INDEX.get(data.getMeatTypeInt()).getRareTemperature());
			record.setSetDonenessRM(BbqConfig.MAP_MEATTYPE_INITIAL_INDEX.get(data.getMeatTypeInt()).getRareTemperature());
			record.setSetDonenessM(BbqConfig.MAP_MEATTYPE_INITIAL_INDEX.get(data.getMeatTypeInt()).getRareTemperature());
			record.setSetDonenessMW(BbqConfig.MAP_MEATTYPE_INITIAL_INDEX.get(data.getMeatTypeInt()).getRareTemperature());
			record.setSetDonenessW(BbqConfig.MAP_MEATTYPE_INITIAL_INDEX.get(data.getMeatTypeInt()).getRareTemperature());

			return  record;
		}
		
		public ArrayList<Float> getEveryHalfMinuteTemperature(BBQDataSet currentData){
			allTemperature.add(currentData.getProbeTemperature());
	
			return  allTemperature;
		}

		public void writeTempToFile() {
			try {
				filepath1 = Environment.getExternalStorageDirectory() + File.separator + System.currentTimeMillis() + ".txt";
				File file = new File(filepath1); 
	            DataOutputStream dataOut = new DataOutputStream(
	                    new FileOutputStream(file));
	            for (int i = 0; i < allTemperature.size(); i++) {
	                dataOut.writeFloat(allTemperature.get(i));
	            }
	            dataOut.flush();
	            dataOut.close();
	        } catch (IOException ex) {
	            System.err.println(ex.getMessage());
	        } finally {
	        	
	        }

		}
		
		 public ArrayList<Float> readTempFromFile(String filepath) {
		        try {
		            DataInputStream dataIn = new DataInputStream(new FileInputStream(filepath));
		            ArrayList<Float> ff = new ArrayList<Float>(); 
		                                                           
		            float f = 0.0F;
		            do {
		                try {
		                    f = dataIn.readFloat();
		                } catch (Exception ex) {
		                    break;
		                }
		                ff.add(f);
		            } while (dataIn.available() != 0);
		            dataIn.close();
		 
		            return ff;
		        } catch (FileNotFoundException ex) {
		            System.out.println(ex.getMessage());
		        } catch (IOException ex) {
		            System.out.println(ex.getMessage());
		        }
		        return null;
		    }

		public void  recyle() {
			allTemperature.clear();
		}

}

