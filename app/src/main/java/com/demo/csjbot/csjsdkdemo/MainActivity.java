package com.demo.csjbot.csjsdkdemo;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.csjbot.coshandler.core.CsjRobot;
import com.csjbot.coshandler.listener.OnCameraListener;
import com.csjbot.coshandler.listener.OnDetectPersonListener;
import com.csjbot.coshandler.listener.OnFaceListener;
import com.csjbot.coshandler.listener.OnGoRotationListener;
import com.csjbot.coshandler.listener.OnHeadTouchListener;
import com.csjbot.coshandler.listener.OnNaviListener;
import com.csjbot.coshandler.listener.OnPositionListener;
import com.csjbot.coshandler.listener.OnSpeechListener;
import com.csjbot.coshandler.listener.OnWakeupListener;
import com.csjbot.coshandler.log.CsjlogProxy;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    CsjRobot mCsjBot;

    RobotPose robotPose1;

    RobotPose robotPose2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCsjBot = CsjRobot.getInstance();

        // 每次开机连接机器人之后要加载地图
//        mCsjBot.getAction().loadMap();

        // 唤醒
        mCsjBot.registerWakeupListener(new OnWakeupListener() {
            @Override
            public void response(int i) {
                Log.d("TAG","registerWakeupListener:i:"+i);
                mCsjBot.getTts().startSpeaking("我在呢!",null);
                mCsjBot.getAction().moveAngle(i, new OnGoRotationListener() {
                    @Override
                    public void response(int i) {
                        if (i > 0 && i < 360) {
                            if (i <= 180) {
                                CsjlogProxy.getInstance().debug("向左转:+" + i);
                                if (mCsjBot.getState().getChargeState() == CsjRobot.State.NOT_CHARGING) {
                                    mCsjBot.getAction().moveAngle(i,null);
                                }
                            } else {
                                CsjlogProxy.getInstance().debug("向右转:-" + (360 - i));
                                if (mCsjBot.getState().getChargeState() == CsjRobot.State.NOT_CHARGING) {
                                    mCsjBot.getAction().moveAngle(-(360 - i),null);
                                }
                            }
                        }
                    }
                });
            }
        });

        // 语音识别
        mCsjBot.registerSpeechListener(new OnSpeechListener() {
            @Override
            public void speechInfo(String s, int i) {

                Log.d("TAG","registerSpeechListener:s:"+s);
                if(CsjRobot.Speech.SPEECH_RECOGNITION_RESULT == i){ // 识别到的信息

                }else if(CsjRobot.Speech.SPEECH_RECOGNITION_AND_ANSWER_RESULT == i){// 识别到的信息与的回答

                }
            }
        });

        // 摄像头实时图片传输
        mCsjBot.registerCameraListener(new OnCameraListener() {
            @Override
            public void response(Bitmap bitmap) {

            }
        });

        // 综合人体检测
        mCsjBot.registerDetectPersonListener(new OnDetectPersonListener() {
            @Override
            public void response(int i) {
                if(i == 0){ // 无人
                    Log.d("TAG","registerDetectPersonListener:无人");
                }else if(i == 1){ // 有人
                    Log.d("TAG","registerDetectPersonListener:有人");
                }
            }
        });

        // 人脸信息
        mCsjBot.registerFaceListener(new OnFaceListener() {
            @Override
            public void personInfo(String s) {
                // 人脸识别到的会员信息(人脸一直在就会持续触发)
                Log.d("TAG","registerFaceListener:s:"+s);
            }

            @Override
            public void personNear(boolean b) {

                if(b){// 有人脸靠近
                    Log.d("TAG","registerFaceListener:有人脸靠近");
                }else{// 人脸消失
                    Log.d("TAG","registerFaceListener:人脸消失");
                }
            }
        });

        // 机器人头部传感器(小雪)
        mCsjBot.registerHeadTouchListener(new OnHeadTouchListener() {
            @Override
            public void response() {
                Log.d("TAG","registerHeadTouchListener:头部传感器触发");
            }
        });

        findViewById(R.id.bt_alice_arm_right_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCsjBot.getAction().AliceRightArmUp();
            }
        });

        findViewById(R.id.bt_alice_arm_right_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCsjBot.getAction().AliceRightArmDown();
            }
        });

        findViewById(R.id.bt_happy_expression).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCsjBot.getExpression().happy();
            }
        });

        findViewById(R.id.bt_save_point1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCsjBot.getAction().getPosition(new OnPositionListener() {
                    @Override
                    public void positionInfo(String s) {
                        Log.d("TAG","OnPositionListener:s:"+s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String rotation = jsonObject.getString("rotation");
                            String x = jsonObject.getString("x");
                            String y = jsonObject.getString("y");
                            String z = jsonObject.getString("z");
                            RobotPose robotPose = new RobotPose();
                            robotPose.setPoseName("位置1");
                            RobotPose.PosBean posBean = new RobotPose.PosBean();
                            posBean.setRotation(Float.valueOf(rotation));
                            posBean.setX(Float.valueOf(x));
                            posBean.setY(Float.valueOf(y));
                            posBean.setZ(Float.valueOf(z));
                            robotPose.setPos(posBean);
                            robotPose1 = robotPose;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void moveResult(String s) {

                    }

                    @Override
                    public void moveToResult(String s) {

                    }

                    @Override
                    public void cancelResult(String s) {

                    }
                });
            }
        });

        findViewById(R.id.bt_save_point2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCsjBot.getAction().getPosition(new OnPositionListener() {
                    @Override
                    public void positionInfo(String s) {
                        Log.d("TAG","OnPositionListener:s:"+s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String rotation = jsonObject.getString("rotation");
                            String x = jsonObject.getString("x");
                            String y = jsonObject.getString("y");
                            String z = jsonObject.getString("z");
                            RobotPose robotPose = new RobotPose();
                            robotPose.setPoseName("位置2");
                            RobotPose.PosBean posBean = new RobotPose.PosBean();
                            posBean.setRotation(Float.valueOf(rotation));
                            posBean.setX(Float.valueOf(x));
                            posBean.setY(Float.valueOf(y));
                            posBean.setZ(Float.valueOf(z));
                            robotPose.setPos(posBean);
                            robotPose2 = robotPose;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void moveResult(String s) {

                    }

                    @Override
                    public void moveToResult(String s) {

                    }

                    @Override
                    public void cancelResult(String s) {

                    }
                });
            }
        });

        findViewById(R.id.bt_navi_point1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(robotPose1 == null){
                    return;
                }
                mCsjBot.getAction().navi(new Gson().toJson(robotPose1.getPos()), new OnNaviListener() {
                    @Override
                    public void moveResult(String s) {
                        mCsjBot.getTts().startSpeaking(robotPose1.getPoseName()+"已经到啦!",null);
                    }

                    @Override
                    public void moveToResult(String s) {

                    }

                    @Override
                    public void cancelResult(String s) {

                    }

                    @Override
                    public void goHome() {

                    }
                });
            }
        });

        findViewById(R.id.bt_navi_point2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(robotPose2 == null){
                    return;
                }
                mCsjBot.getAction().navi(new Gson().toJson(robotPose2.getPos()), new OnNaviListener() {
                    @Override
                    public void moveResult(String s) {
                        mCsjBot.getTts().startSpeaking(robotPose2.getPoseName()+"已经到啦!",null);
                    }

                    @Override
                    public void moveToResult(String s) {

                    }

                    @Override
                    public void cancelResult(String s) {

                    }

                    @Override
                    public void goHome() {

                    }
                });
            }
        });

        findViewById(R.id.bt_save_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCsjBot.getAction().saveMap();
            }
        });

        // 开启语音识别(多次)
        mCsjBot.getSpeech().startIsr();

    }
}
