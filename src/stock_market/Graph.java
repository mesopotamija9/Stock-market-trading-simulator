package stock_market;

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Formatter;

import javax.swing.JPanel;

public class Graph extends JPanel {
	private App app;
	int scale = 1;
	private int candlesOutsideScreenRight = 0;
	private String priceStr = "";
	private int candleToShowIndex = 0;
	
	private int offsetX = 0;
	private int offsetY = 0;
	
	private int startPanX = 0;
	private int startPanY = 0;
	
	private double minLow = 0;
	private double maxHigh = 0;
	
	boolean stockLoaded = false;
	int candleWidth = 10;
	int offset = 15;
	
	private int mouseX;
	private int mouseY;
	
	public Graph(App app) {
		this.app = app;
		setPreferredSize(new Dimension(700, 500));
		
		
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
				
				startPanX = (int) mouseLoc.getX();
				startPanY = (int) mouseLoc.getY();
				
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
//					if (candleWidth >= 30)
//						return;
					candleWidth += 2;
					offset += 2;
				}
				
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
//					if (candleWidth <= 10)
//						return;
					candleWidth -= 2;
					offset -= 2;
				}
					
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					minLow += 0.5;
					maxHigh += 0.5;
				}
				
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					minLow -= 0.5;
					maxHigh -= 0.5;
				}
				
				repaint();
			}
		});
		
		this.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
					if(e.getWheelRotation() == 1) {
						// Down
						

						if (maxHigh - minLow <= 500) {
							minLow -= 1;
							maxHigh += 1;
						}
					} else {
						// Up
						if (maxHigh - minLow >= 2) {
							minLow += 1;
							maxHigh -= 1;
						}
							
					}
					
				} 
				
				repaint();
				
			}
			
		});
		
		addMouseListener(new MouseAdapter() {

			@Override
			public void mousePressed(MouseEvent e) {
				startPanX = e.getX();
//				startPanY = e.getY();
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				requestFocus();
			}
		});
		
		addMouseMotionListener(new MouseMotionAdapter() {

			@Override
			public void mouseDragged(MouseEvent e) {
				offsetX -= (e.getX() - startPanX);
//				offsetY -= (e.getY() - startPanY);
				
				startPanX = e.getX();
//				startPanY = e.getY();
				
				mouseX = e.getX();
				mouseY = e.getY();
				
				repaint();
			}
			
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseX = e.getX();
				mouseY = e.getY();
				
				if (Graph.this.app.getStock() != null) {
					FontMetrics fontMetric = Graph.this.getFontMetrics(getFont());
					int startOffset = Graph.this.getWidth() - offset - fontMetric.stringWidth(priceStr) + 5;
					
					candleToShowIndex = (startOffset - mouseX + offsetX) / offset;
					
					ArrayList<Candle> candles = Graph.this.app.getStock().getCandles();
					
					if (candleToShowIndex < 0)
						candleToShowIndex = 0;
					if (candleToShowIndex > candles.size() - 1)
						candleToShowIndex = candles.size() - 1;
					
					int i = candles.size() - 1 - candleToShowIndex;
					
					String openStr = new Formatter().format("%.2f", candles.get(i).getOpen()).toString();
					String highStr = new Formatter().format("%.2f", candles.get(i).getHigh()).toString();
					String closeStr = new Formatter().format("%.2f", candles.get(i).getClose()).toString();
					String lowStr = new Formatter().format("%.2f", candles.get(i).getLow()).toString();
					
					Color color;
					
					if (candles.get(i).getOpen() <= candles.get(i).getClose()) {
						color = Color.decode("#26a69a");
					} else {
						color = Color.decode("#ef5350");
					}
					
					Graph.this.app.openLabel.setForeground(color);
					Graph.this.app.openLabel.setText(openStr + " $");
					Graph.this.app.openLabel.revalidate();
					
					Graph.this.app.highLabel.setForeground(color);
					Graph.this.app.highLabel.setText(highStr + " $");
					Graph.this.app.highLabel.revalidate();
					
					Graph.this.app.closeLabel.setForeground(color);
					Graph.this.app.closeLabel.setText(closeStr + " $");
					Graph.this.app.closeLabel.revalidate();
					
					Graph.this.app.lowLabel.setForeground(color);
					Graph.this.app.lowLabel.setText(lowStr + " $");
					Graph.this.app.lowLabel.revalidate();
					
					Graph.this.app.timestampLabel.setForeground(color);
					Graph.this.app.timestampLabel.setText(String.valueOf(candles.get(i).getTimestamp()));
					Graph.this.app.timestampLabel.revalidate();
				}
				
				repaint();
			}
		});

		setBackground(Color.white);
		setSize(new Dimension(700,500));
		
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		if (app.getStock() == null) {
			return;
		}
		
		int width = getWidth();
		int height = getHeight();
		
		
		
		if (!stockLoaded) {
			minLow = app.getStock().getMinLow();
			maxHigh = app.getStock().getMaxHigh();
			stockLoaded = true;
		}
		
		double yPixelValue = (maxHigh - minLow) / height;
		
		for (int i = 0; i < width; i += width / 12) {
			g.setColor(Color.decode("#e8eded"));
			g.drawLine(i, 0, i, height -1);
			g.setColor(Color.black);
		}

		FontMetrics fontMetric = g.getFontMetrics(getFont());
		
		for (int i = 0; i < height; i += height / 12) {
			g.setColor(Color.decode("#e8eded"));
			g.drawLine(0, i, width - 1, i);
			
			
			g.setColor(Color.black);
			if (i == 0) continue;
			
			priceStr = new Formatter().format("%.2f",maxHigh - yPixelValue * i).toString();
			g.drawString(priceStr, width - fontMetric.stringWidth(priceStr), i + 5);
		}
		
		
		// Draw mouse position
		for (int i = 0; i < height; i += height / 40) {
			g.drawLine(mouseX, i, mouseX, i + 5);
		}
		for (int i = 0; i < width; i += height / 40) {
			g.drawLine(i, mouseY, i + 5, mouseY);
		}
		
//		String priceYStr = priceStr = new Formatter().format("%.2f",maxHigh - yPixelValue * mouseY).toString();
//		g.fillRect(width - fontMetric.stringWidth(priceStr) - 2, mouseY - 5, fontMetric.stringWidth(priceStr) + 2, 12);
//		g.setColor(Color.white);
//		g.drawString(priceYStr, width - fontMetric.stringWidth(priceStr), mouseY + 5);
//		g.setColor(Color.black);
		
		ArrayList<Candle> candles =  app.getStock().getCandles();

		
		int x = width - offset - fontMetric.stringWidth(priceStr);
		
		
		
		Candle c;
		int j = 0;
		candlesOutsideScreenRight = 0;
		for (int i = candles.size() - 1; i >= 0; i--) {
			c = candles.get(i);
			
			int y1 = (int)((maxHigh - c.getHigh()) / yPixelValue);
			int y2 = (int)((maxHigh - c.getLow()) / yPixelValue);
			
			double open = c.getOpen();
			double close = c.getClose();
			
			double upper;
			double lower;
			
			if (close >= open) {
				g.setColor(Color.decode("#26a69a"));
				upper = close;
				lower = open;
			} else {
				g.setColor(Color.decode("#ef5350"));
				upper = open;
				lower = close;
			}
			
			int y11 = (int)((maxHigh - upper) / yPixelValue);
			int rectHeight = (int)((upper - lower) / yPixelValue);
			
			g.drawLine(x + offsetX, y1 + offsetY, x + offsetX, y2 + offsetY);
			
			g.fillRect(x - candleWidth / 2 + offsetX, y11, candleWidth, rectHeight);
			
			if (x - candleWidth / 2 + offsetX > width) {
				candlesOutsideScreenRight++;
			}
			
			x -= offset;
			//System.out.println(candlesOutsideScreenRight);
		}
		
		String nStr = app.getNTextField().getText();
		int n;
		
		if (app.getShowMA().isSelected()) {
			if (nStr.length() == 0) {
				app.getIndicatorsErrorLabel().setText("Enter n");
				app.getIndicatorsErrorLabel().revalidate();
			} else {
				try {
					n = Integer.valueOf(nStr);
					app.getIndicatorsErrorLabel().setText("");
					app.getIndicatorsErrorLabel().revalidate();
					
					x = width - offset - fontMetric.stringWidth(priceStr);
					for (int i = app.getStock().getSize() - 1; i > 0; i--) {
						
						
						int y1 = (int)((maxHigh - app.getStock().getMA(n, i)) / yPixelValue);
						int y2 = (int)((maxHigh - app.getStock().getMA(n, i - 1)) / yPixelValue);
						
						g.setColor(Color.yellow);
						g.drawLine(x + offsetX, y1 + offsetY, x - offset + offsetX, y2 + offsetY);
						
						x -= offset;
					}
				} catch (Exception e) {
					app.getIndicatorsErrorLabel().setText("Invalid n");
					app.getIndicatorsErrorLabel().revalidate();
				}
			}
		}
		if (app.getShowEMA().isSelected()) {
			if (nStr.length() == 0) {
				app.getIndicatorsErrorLabel().setText("Enter n");
				app.getIndicatorsErrorLabel().revalidate();
			} else {
				try {
					n = Integer.valueOf(nStr);
					app.getIndicatorsErrorLabel().setText("");
					app.getIndicatorsErrorLabel().revalidate();
					
					x = width - offset - fontMetric.stringWidth(priceStr);
					for (int i = app.getStock().getSize() - 1; i > 0; i--) {
						
						
						int y1 = (int)((maxHigh - app.getStock().getEMA(n, i)) / yPixelValue);
						int y2 = (int)((maxHigh - app.getStock().getEMA(n, i - 1)) / yPixelValue);
						
						g.setColor(Color.blue);
						g.drawLine(x + offsetX, y1 + offsetY, x - offset + offsetX, y2 + offsetY);
						
						x -= offset;
					}
				} catch (Exception e) {
					app.getIndicatorsErrorLabel().setText("Invalid n");
					app.getIndicatorsErrorLabel().revalidate();
				}
			}
		}
		
		g.setColor(Color.black);
		String priceYStr = priceStr = new Formatter().format("%.2f",maxHigh - yPixelValue * mouseY).toString();
		g.fillRect(width - fontMetric.stringWidth(priceStr) - 2, mouseY - 5, fontMetric.stringWidth(priceStr) + 2, 12);
		g.setColor(Color.white);
		g.drawString(priceYStr, width - fontMetric.stringWidth(priceStr), mouseY + 5);
		g.setColor(Color.black);
	}
}
