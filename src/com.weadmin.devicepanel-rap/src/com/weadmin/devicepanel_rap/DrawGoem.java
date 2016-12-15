package com.weadmin.devicepanel_rap;

import com.weadmin.devicepanel_rap.models.svPoint;
import com.weadmin.devicepanel_rap.models.svellipsearc;
import com.weadmin.devicepanel_rap.models.vsGeometry;

public class DrawGoem {
	public static String sDL ="";
	public static long PixPerInch = 0;
	public static double PageScale = 1;
	public static float GoemHeight = 100;
	public static float GoemWidth = 100;
	public static String crLineColor;
	public static String crFillColor;
	public static String crFillOpacity="1";
	public static long lLineWith;
	public static SvgMap svgchart;
	public static String sPenStyle;
	public static float x1 = 0;
	public static float y1 = 0;
	public static double Angle = 0;
	public static String svid="";
	public static String FlipX="0";
	public static String FlipY="0";
	public DrawGoem() {

		// TODO Auto-generated constructor stub
	}
	/**
	 * 划线
	 * @param Geometrs
	 * @return
	 */
	public static String DrawLineTo(vsGeometry[] Geometrs)
	{
		String pathdata="";
		svPoint MovePoint=new svPoint();
		MovePoint.x = MovePoint.y = 0;
		for(vsGeometry Geometry:Geometrs)
		{
			if(Geometry==null)
			{
              continue;
			}
			if(Geometry.name.equals("MoveTo"))
			{
				MovePoint.x =Geometry.x;
				MovePoint.y =Geometry.y;
				FlipYAndFloor(MovePoint);
				pathdata+="M"+MovePoint.x+","+MovePoint.y+" ";

			}else if (Geometry.name.equals("LineTo"))
			{
				MovePoint.x =Geometry.x;
				MovePoint.y =Geometry.y;
				FlipYAndFloor(MovePoint);
				pathdata+="L"+MovePoint.x+","+MovePoint.y+" ";
			}else if (Geometry.name.equals("PolylineTo"))
			{
				String ShapeData = Geometry.a;
				int startindex = ShapeData.indexOf('(');
				int endindex = ShapeData.indexOf(')');
				if ((startindex != -1) && (endindex != -1))
				{
					ShapeData = ShapeData.substring(startindex + 1, startindex + 1 + endindex - startindex - 1);
					String [] ShapeDataPoints = ShapeData.split("[,]", -1);
					int Points = ShapeDataPoints.length/2;
					svPoint[] Pointdata = new svPoint[Points];
					for(int p=0;p<Points;p++)
					{
						Pointdata[p]=new svPoint();
						Pointdata[p].x = Float.parseFloat(ShapeDataPoints[p*2])*100;
						Pointdata[p].y = Float.parseFloat(ShapeDataPoints[p*2+1])*100;
					}
					FlipY(Pointdata);
					String sPoints="";
					String closePoints="";
					//第一个点多余点所以排除掉
					for(int p=1;p<Points;p++)
					{
						if(p==1)
						{
							closePoints=" "+Pointdata[p].x*GoemWidth/100+","+Pointdata[p].y*GoemHeight/100;
						}
						sPoints+=Pointdata[p].x*GoemWidth/100+","+Pointdata[p].y*GoemHeight/100+" ";
					}
					ShapeItem polylineitem=new ShapeItem(svgchart);
					if(!svid.equals(""))
					{
						polylineitem.setSvid(svid);
					}
					polylineitem.setFillcolor(crFillColor);
					//polylineitem.setFillopacity(crFillOpacity);
					polylineitem.setLinecolor(crLineColor);
					polylineitem.setLinewidth((int)lLineWith);
					polylineitem.setItemid("g2");
					String trans="translate("+x1+","+y1+")";
					if(Angle!=0)
					{
						trans+=GetAngle(Angle);
					}
					polylineitem.setTransform(trans);
					polylineitem.setSvgtype(5);
					polylineitem.setValue(sPoints.trim()+closePoints);


				}
				pathdata="";
			}else if (Geometry.name.equals("ArcTo"))
			{
				svPoint Point = new svPoint();
				Point.x = Geometry.x;
				Point.y = Geometry.y;
				PointFlipY(Point);
				float a = (float)Unit2Pix(sDL, "MM",Geometry.a);
				float b = Math.abs(a);
				float r = b/2 + (float)(Math.pow(Point.x-MovePoint.x,2) + Math.pow(Point.y-MovePoint.y,2))/(8*b);
				pathdata+="A "+r +","+r +" 0 0,0 "+Point.x+","+Point.y;
			}else if (Geometry.name.equals("EllipticalArcTo"))
			{
				svPoint Point = new svPoint();
				Point.x = Geometry.x;
				Point.y = Geometry.y;
				PointFlipY(Point);
				float a = (float)Unit2Pix(sDL, "MM",Geometry.a);
				float b = (float)Unit2Pix(sDL, "MM", Geometry.b);
				float c = Float.parseFloat(Geometry.c);
				float d = Float.parseFloat(Geometry.d);
				svPoint center_point = new svPoint();
				center_point.x=a;
				center_point.y=b;
				PointFlipY(center_point);
				svellipsearc ellipsearc=new svellipsearc();
				DrawEllipticalArcLine(MovePoint,Point,center_point,d,c,ellipsearc);
				pathdata+="A "+ellipsearc.rx+","+ellipsearc.ry+" "+c+" 0,"+ellipsearc.sweepflag+" "+Point.x+","+Point.y;


			}
		}
		if(!pathdata.equals(""))
		{
			ShapeItem pathitem=new ShapeItem(svgchart);
			if(!svid.equals(""))
			{
				pathitem.setSvid(svid);
			}
			pathitem.setFillcolor(crFillColor);
			pathitem.setLinecolor(crLineColor);
			//pathitem.setFillopacity(crFillOpacity);
			pathitem.setLinewidth((int)lLineWith);
			pathitem.setItemid("g2");
			String trans="translate("+x1+","+y1+")";
			if(Angle!=0)
			{
				trans+=GetAngle(Angle);
			}
			pathitem.setTransform(trans);
			pathitem.setSvgtype(2);
			pathitem.setValue(pathdata.trim()+"Z");
		}

		return "";
	}
	/**
	 * 返回相关path 函数和数值
	 * @return
	 */
	public static String DrawPathTo(vsGeometry[] Geometrs)
	{

		String pathdata="";
		svPoint MovePoint=new svPoint();
		MovePoint.x = MovePoint.y = 0;
		for(vsGeometry Geometry:Geometrs)
		{
			if(Geometry==null)
			{
              continue;
			}
			if(Geometry.name.equals("MoveTo"))
			{
				MovePoint.x =Geometry.x;
				MovePoint.y =Geometry.y;
				FlipYAndFloor(MovePoint);
				pathdata+="M"+MovePoint.x+","+MovePoint.y+" ";

			}else if (Geometry.name.equals("LineTo"))
			{
				MovePoint.x =Geometry.x;
				MovePoint.y =Geometry.y;
				FlipYAndFloor(MovePoint);
				pathdata+="L"+MovePoint.x+","+MovePoint.y+" ";
			}else if (Geometry.name.equals("PolylineTo"))
			{
				String ShapeData = Geometry.a;
				int startindex = ShapeData.indexOf('(');
				int endindex = ShapeData.indexOf(')');
				if ((startindex != -1) && (endindex != -1))
				{
					ShapeData = ShapeData.substring(startindex + 1, startindex + 1 + endindex - startindex - 1);
					String [] ShapeDataPoints = ShapeData.split("[,]", -1);
					int Points = ShapeDataPoints.length/2;
					svPoint[] Pointdata = new svPoint[Points];
					for(int p=0;p<Points;p++)
					{
						Pointdata[p]=new svPoint();
						Pointdata[p].x = Float.parseFloat(ShapeDataPoints[p*2])*100;
						Pointdata[p].y = Float.parseFloat(ShapeDataPoints[p*2+1])*100;
					}
					FlipY(Pointdata);
					String sPoints="";
					String closePoints="";
					//第一个点多余点所以排除掉
					for(int p=1;p<Points;p++)
					{
						if(p==1)
						{
							closePoints=" "+Pointdata[p].x*GoemWidth/100+","+Pointdata[p].y*GoemHeight/100;
						}
						sPoints+=Pointdata[p].x*GoemWidth/100+","+Pointdata[p].y*GoemHeight/100+" ";
					}
					svPoint lastPointdata = new svPoint();
					lastPointdata.x = Geometry.x;
					lastPointdata.y = Geometry.y;
					FlipYAndFloor(lastPointdata);
					sPoints+=lastPointdata.x+","+lastPointdata.y;
					ShapeItem polylineitem=new ShapeItem(svgchart);
					if(!svid.equals(""))
					{
						polylineitem.setSvid(svid);
					}
					polylineitem.setFillcolor(crFillColor);
					polylineitem.setLinecolor(crLineColor);
					polylineitem.setLinewidth(1);
					polylineitem.setItemid("g2");
					String trans="translate("+x1+","+y1+")";

					trans+=GetAngle(Angle);

					polylineitem.setTransform(trans);
					polylineitem.setSvgtype(5);
					polylineitem.setValue(sPoints.trim()+closePoints);

				}
				pathdata="";
			}else if (Geometry.name.equals("ArcTo"))
			{
				svPoint Point = new svPoint();
				Point.x = Geometry.x;
				Point.y = Geometry.y;
				PointFlipY(Point);
				float a = (float)Unit2Pix(sDL, "MM",Geometry.a);
				float b = Math.abs(a);
				float r = b/2 + (float)(Math.pow(Point.x-MovePoint.x,2) + Math.pow(Point.y-MovePoint.y,2))/(8*b);
				pathdata+="A "+r +","+r +" 0 0,0 "+Point.x+","+Point.y;
			}else if (Geometry.name.equals("EllipticalArcTo"))
			{
				svPoint Point = new svPoint();
				Point.x = Geometry.x;
				Point.y = Geometry.y;
				PointFlipY(Point);
				float a = (float)Unit2Pix(sDL, "MM",Geometry.a);
				float b = (float)Unit2Pix(sDL, "MM", Geometry.b);
				float c = Float.parseFloat(Geometry.c);
				float d = Float.parseFloat(Geometry.d);
				svPoint center_point = new svPoint();
				center_point.x=a;
				center_point.y=b;
				PointFlipY(center_point);
				svellipsearc ellipsearc=new svellipsearc();
				DrawEllipticalArcLine(MovePoint,Point,center_point,d,c,ellipsearc);
				pathdata+="A "+ellipsearc.rx+","+ellipsearc.ry+" "+c+" 0,0 "+Point.x+","+Point.y;

			}
		}
		if(!pathdata.equals(""))
		{
			ShapeItem pathitem=new ShapeItem(svgchart);
			if(!svid.equals(""))
			{
				pathitem.setSvid(svid);
			}
			pathitem.setFillcolor(crFillColor);
			pathitem.setLinecolor(crLineColor);
			pathitem.setLinewidth(1);
			pathitem.setItemid("g2");
			String trans="translate("+x1+","+y1+")";

			trans+=GetAngle(Angle);
			pathitem.setTransform(trans);
			pathitem.setSvgtype(2);
			pathitem.setValue(pathdata.trim()+"Z");
		}

		return "";
	}
	/**
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param ab 长短比值
	 * @param deg 度数
	 */
	public static void DrawEllipticalArcLine( svPoint p1, svPoint p2, svPoint p3, float ab, float deg,svellipsearc ellipsearc)
	{
		double testangle = Math.round(deg*180/Math.PI*100)/100;
		if (testangle % 360 == 0)
		{
			DrawStandEllipticalArcLine( p1.clone(), p2.clone(), p3.clone(), ab,ellipsearc);
		}
		else
		{
			if (testangle % 90 == 0)
			{
				DrawStandEllipticalArcLine( p1.clone(), p2.clone(), p3.clone(), 1/ab,ellipsearc);
			}
			else
			{
				DrawRevolvingEllipticalArcLine( p1.clone(), p2.clone(), p3.clone(), ab, -deg,ellipsearc);
			}
		}
	}
	/**
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 * @param ab
	 */
	public static void DrawStandEllipticalArcLine( svPoint p1, svPoint p2, svPoint p3, float ab,svellipsearc ellipsearc)
	{
		float x0,y0;
		float xa = p1.x;
		float ya = p1.y;
		float xb = p2.x;
		float yb = p2.y;
		float xc = p3.x;
		float yc = p3.y;
		/**2x0(xb-xa) -2F2y0(ya-yb) = F 2yb2 + xb2 - F 2ya2 - xa2
		*/
		if ((xa == xb) && (ya != yb))
		{
			y0 = (Pow(ab)*Pow(yb) + Pow(xb) - Pow(ab)*Pow(ya) - Pow(xa)) / (2*Pow(ab)*(yb - ya));
			x0 = (Pow(ab)*Pow(yc) + Pow(xc) - Pow(ab)*Pow(ya) - Pow(xa)) / (2*(xc - xa)) + Pow(ab)*(ya-yc)/(xc -xa)*y0;
		}
		else if ((xa == xc) && (ya != yc))
		{
			y0 = (Pow(ab)*Pow(yc) + Pow(xc) - Pow(ab)*Pow(ya) - Pow(xa)) / (2*Pow(ab)*(yc - ya));
			x0 = (Pow(ab)*Pow(yb) + Pow(xb) - Pow(ab)*Pow(ya) - Pow(xa)) / (2*(xb - xa)) + Pow(ab)*(ya-yb)/(xb -xa)*y0;
		}
		else if ((ya == yb) && (xa != xb))
		{
			x0 = (Pow(ab)*Pow(yb) + Pow(xb) - Pow(ab)*Pow(ya) - Pow(xa)) / (2*(xb - xa));
			y0 = (2*x0*(xc-xa) - Pow(ab)*Pow(yc) - Pow(xc) + Pow(ab)*Pow(ya) + Pow(xa))/(2 * Pow(ab) * (ya-yc));
		}
		else if ((ya == yc) && (xa != xc))
		{
			x0 = (Pow(ab)*Pow(yc) + Pow(xc) - Pow(ab)*Pow(ya) - Pow(xa)) / (2*(xc - xa));
			y0 = (2*x0*(xb-xa) - Pow(ab)*Pow(yb) - Pow(xb) + Pow(ab)*Pow(ya) + Pow(xa))/(2 * Pow(ab) * (ya-yb));
		}
		else
		{
			y0 = ((Pow(ab)*Pow(yb) + Pow(xb) - Pow(ab)*Pow(ya) - Pow(xa)) / (2*(xb - xa)) - (Pow(ab)*Pow(yc) + Pow(xc) - Pow(ab)*Pow(ya) - Pow(xa)) / (2*(xc - xa))) / (Pow(ab) *((ya -yc)/(xc-xa) - (ya-yb)/(xb-xa)));
			x0 = (Pow(ab)*Pow(yb) + Pow(xb) - Pow(ab)*Pow(ya) - Pow(xa)) / (2*(xb - xa)) + Pow(ab)*(ya-yb)/(xb -xa)*y0;
		}
		/**(xa-x0) 2 + F 2 (ya-y0) 2  =  a 2
		*/
		float a = (float)Math.sqrt(Pow(xa-x0) + Pow(ab) * Pow(ya-y0));
		float b = a/ab;
		ellipsearc.rx=a;
		ellipsearc.ry=b;
		ellipsearc.sweepflag= CalculateEllipticalAngle(p1,p2,p3);

	}
	public static void DrawRevolvingEllipticalArcLine( svPoint p1, svPoint p2, svPoint p3, float ab, float deg,svellipsearc ellipsearc)
	{
		float x0,y0;
		float xa = p1.x;
		float ya = p1.y;
		float xb = p2.x;
		float yb = p2.y;
		float xc = p3.x;
		float yc = p3.y;
		float A = Pow((float)Math.cos(deg)) + Pow(ab)*Pow((float)Math.sin(deg));
		float B = Pow((float)Math.sin(deg)) + Pow(ab)*Pow((float)Math.cos(deg));
		float C = (float)(Math.cos(deg) * Math.sin(deg)*(1-Pow(ab)));

		if ((A*xb+C*yb == A*xa+C*ya) && (B*yb+C*xb != B*ya+C*xa))
		{
			y0 = (A*Pow(xb)+B*Pow(yb)+2*C*xb*yb-A*Pow(xa)-B*Pow(ya)-2*C*xa*ya)/(B*yb+C*xb-B*ya-C*xa)/2;
			x0 = (A*Pow(xc)+B*Pow(yc)+2*C*xc*yc-A*Pow(xa)-B*Pow(ya)-2*C*xa*ya)/(A*xc+C*yc-A*xa-C*ya)/2 - y0*(B*yc+C*xc-B*ya-C*xa)/(A*xc+C*yc-A*xa-C*ya);
		}
		else if ((A*xc+C*yc == A*xa+C*ya) && (B*yc+C*xc != B*ya+C*xa))
		{
			y0 = (A*Pow(xc)+B*Pow(yc)+2*C*xc*yc-A*Pow(xa)-B*Pow(ya)-2*C*xa*ya)/(B*yc+C*xc-B*ya-C*xa)/2;
			x0 = (A*Pow(xb)+B*Pow(yb)+2*C*xb*yb-A*Pow(xa)-B*Pow(ya)-2*C*xa*ya)/(A*xb+C*yb-A*xa-C*ya)/2 - y0*(B*yb+C*xb-B*ya-C*xa)/(A*xb+C*yb-A*xa-C*ya);
		}
		else if ((B*yb+C*xb == B*ya+C*xa) && (A*xb+C*yb != A*xa+C*ya))
		{
			x0 = (A*Pow(xb)+B*Pow(yb)+2*C*xb*yb-A*Pow(xa)-B*Pow(ya)-2*C*xa*ya)/(A*xb+C*yb-A*xa-C*ya)/2;
			y0 = (A*Pow(xc)+B*Pow(yc)+2*C*xc*yc-A*Pow(xa)-B*Pow(ya)-2*C*xa*ya)/(B*yc+C*xc-B*ya-C*xa)/2 - x0*(A*xc+C*yc-A*xa-C*ya)/(B*yc+C*xc-B*ya-C*xa);
		}
		else if ((B*yc+C*xc == B*ya+C*xa) && (A*xc+C*yc != A*xa+C*ya))
		{
			x0 = (A*Pow(xc)+B*Pow(yc)+2*C*xc*yc-A*Pow(xa)-B*Pow(ya)-2*C*xa*ya)/(A*xc+C*yc-A*xa-C*ya)/2;
			y0 = (A*Pow(xb)+B*Pow(yb)+2*C*xb*yb-A*Pow(xa)-B*Pow(ya)-2*C*xa*ya)/(B*yb+C*xb-B*ya-C*xa)/2 - x0*(A*xb+C*yb-A*xa-C*ya)/(B*yb+C*xb-B*ya-C*xa);
		}
		else
		{
			float BB = (A*Pow(xb)+B*Pow(yb)+2*C*xb*yb-A*Pow(xa)-B*Pow(ya)-2*C*xa*ya)/(A*xb+C*yb-A*xa-C*ya)/2;
			float BBB = (B*yb+C*xb-B*ya-C*xa)/(A*xb+C*yb-A*xa-C*ya);
			float CC = (A*Pow(xc)+B*Pow(yc)+2*C*xc*yc-A*Pow(xa)-B*Pow(ya)-2*C*xa*ya)/(A*xc+C*yc-A*xa-C*ya)/2;
			float CCC = (B*yc+C*xc-B*ya-C*xa)/(A*xc+C*yc-A*xa-C*ya);
			y0 = (BB - CC)/ (BBB - CCC);
			x0 = BB - y0 * BBB;
		}

		float a = (float)Math.sqrt(Pow(xa-x0)*A + B*Pow(ya-y0)+2*C*(xa-x0)*(ya-y0));
		float b = a/ab;
		ellipsearc.rx=a;
		ellipsearc.ry=b;
		ellipsearc.sweepflag= CalculateEllipticalAngle(p1,p2,p3);

	}
	/**
	 *
	 * @param p1
	 * @param p2
	 * @param p3
	 * @return sweepflag
	 */
	public static String CalculateEllipticalAngle(svPoint p1, svPoint p2, svPoint p3)
	{
//		math:atan2(v:Y - $lastY, v:X - $lastX)- math:atan2(v:B - $lastY, v:A - $lastX)
		String sweepflag="1";
		double Angle=0.0;
		Angle=Math.atan2(p2.y-p1.y,p2.x-p1.x)-Math.atan2(p3.y-p1.y,p3.x-p1.x);
		if(Angle>0 && Angle<180)
		{
			sweepflag="0";
		}
		if(Angle<0 && Math.abs(Angle) >180)
		{
			sweepflag="0";
		}
		return sweepflag;


	}
	/**
	 * 获取实际旋转角度和方向
	 * @param Angle
	 * @return
	 */
	private static String GetAngle(double Angle)
	{
		String sAngle="";
		String sscale="";
//		if(Math.abs(Angle)>180)
//		{
//			if(Angle>0)
//			{
//				Angle=-(Angle-180);
//			}else
//			{
//				Angle=-(Angle+180);
//			}
//		}
//		if(Angle==-180)
//		{
//			Angle=180;
//		}
		if(Angle!=0)
		{
		sAngle=" rotate("+Math.round(-Angle*100)/100+" "+GoemWidth/2+","+GoemHeight/2+")";
		}else
		{
		double fAngle2=0.0;
	    if (FlipX.equals("1")) {
			fAngle2 = 180;
		}
		if (FlipY.equals("1")) {
			fAngle2 = 180;
		}
		if(fAngle2>0)
		{
			sAngle=" rotate("+Math.round(fAngle2*100)/100+" "+GoemWidth/2+","+GoemHeight/2+")";
		}
		}
		/**
		if(FlipX.equals("0") && FlipY.equals("0"))
		{
			sAngle=" rotate( "+ Math.round(-Angle*100)/100+" "+GoemWidth/2+","+GoemHeight/2+")";
		}else if(FlipX.equals("0") && FlipY.equals("1"))
		{

			if(Math.abs(Angle)>0 && Math.abs(Angle)<=90)
			{
				sscale= " scale(1,-1)";
				sAngle=" rotate( "+ Math.round(-Angle*100)/100+" "+GoemWidth/2+","+GoemHeight/2+")";
			}
			if(Math.abs(Angle)>90 && Math.abs(Angle)<180)
			{
				sscale= " scale(-1,1)";
				if(Angle>0)
				{
					sAngle=" rotate( "+ Math.round((180.0-Angle)*100)/100+" "+GoemWidth/2+","+GoemHeight/2+")";
				}
				else
                {
					sAngle=" rotate( -"+ Math.round((180.0+Angle)*100)/100+" "+GoemWidth/2+","+GoemHeight/2+")";
                }

			}

			sAngle+=sscale;
		}else if(FlipX.equals("1") && FlipY.equals("0"))
		{
			sscale= " scale(-1,1)";
			if(Math.abs(Angle)>0 && Math.abs(Angle)<=90)
			{
				sscale= " scale(-1,1)";
				sAngle=" rotate( "+ Math.round(-Angle*100)/100+" "+GoemWidth/2+","+GoemHeight/2+")";
			}
			if(Math.abs(Angle)>90 && Math.abs(Angle)<180)
			{
				sscale= " scale(1,-1)";
				if(Angle>0)
				{
					sAngle=" rotate( "+ Math.round((180.0-Angle)*100)/100+" "+GoemWidth/2+","+GoemHeight/2+")";
				}
				else
                {
					sAngle=" rotate( -"+ Math.round((180.0+Angle)*100)/100+" "+GoemWidth/2+","+GoemHeight/2+")";
                }

			}

			sAngle+=sscale;

		}else if(FlipX.equals("1") && FlipY.equals("1"))
		{
			if(Angle>0)
			{
				sAngle=" rotate( "+ Math.round((180.0-Angle)*100)/100+" "+GoemWidth/2+","+GoemHeight/2+")";
			}else
			{
				sAngle=" rotate( -"+ Math.round((180.0+Angle)*100)/100+" "+GoemWidth/2+","+GoemHeight/2+")";
			}

		}else
		{}
		*/
//		if(Angle>360)
//		{
//			sAngle=" rotate( -"+ Math.round((Angle-360.0)*100)/100+" "+GoemWidth/2+","+GoemHeight/2+")";
//		}else
//		{
//			sAngle=" rotate("+Math.round(Angle*100)/100+" "+GoemWidth/2+","+GoemHeight/2+")";
//		}

		return sAngle;

	}
	/**
	 * 得到像素值
	 * @param Unit
	 * @param DefaultUnit
	 * @param Value
	 * @return
	 */
	private static double Unit2Pix(String Unit, String DefaultUnit, String Value)
	{
		String sUnit = Unit, sDefaultUnit = DefaultUnit;
		double fVal, fRes;

		// Setting decimal separator to ','
		//Value = Value.replace(".",sSeparator);
		fVal = Double.parseDouble(Value);

		sUnit = sUnit.toLowerCase();
		sDefaultUnit = sDefaultUnit.toLowerCase();

		if (sUnit.equals("dl"))
		{
			sUnit = sDefaultUnit;
		}

		if (sUnit.equals("in"))
		{
			fRes = fVal*PixPerInch;
		}
		else if (sUnit.equals("mm"))
		{
			fRes = (double)(fVal/24.4)*PixPerInch;
		}
		else if (sUnit.equals("cm"))
		{
			fRes = (double)(fVal/2.44)*PixPerInch;
		}
		else
		{
			fRes = 0;
		}

		if (!sUnit.equals(sDefaultUnit))
		{
			fRes = fRes * PageScale;
		}

		return fRes;
	}
	/**
	 * 修正y轴坐标
	 * 坐标为实际坐标
	 * @param sourcePoint
	 */
	private static void PointFlipY(svPoint sourcePoint)
	{
		sourcePoint.y =  (float)GoemHeight - sourcePoint.y ;
	}
	/**
	 * 修正坐标点
	 * 坐标为实际坐标
	 * @param sourcePoint
	 */
	private static void FlipYAndFloor(svPoint sourcePoint)
	{
		sourcePoint.y= (float)GoemHeight - sourcePoint.y ;
		if (sourcePoint.y < 0)
		{
			sourcePoint.y = 0;
		}
		sourcePoint.x = sourcePoint.x ;
	}
	/**
	 * 修正y轴坐标
	 * 坐标为百分比
	 * @param sourcePoint
	 */
	private static void FlipY(svPoint[] sourcePoint)
	{
		for(int i=0;i<sourcePoint.length;i++)
		{
			sourcePoint[i].y = (float)100 - sourcePoint[i].y;
		}
	}
	/**
	 * 求平方
	 * @param xx
	 * @return
	 */
	public static float Pow(float xx)
	{
		return xx * xx;
	}

}
