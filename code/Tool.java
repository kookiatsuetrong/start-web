/**
 * Collections of common tools.
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import javax.imageio.ImageIO;


class Tool {
	
	/*
	*	Creates a photo of the given string.
	*/
	public static String createPhotoCode(final String text) {
		
		final int height = 16;
		final int width = 42;
		BufferedImage image = new BufferedImage(width, height,
									BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = image.createGraphics();
		g.setFont(new Font( Font.MONOSPACED, Font.PLAIN, height) );
		g.setColor(new Color(64, 64, 64));
		g.drawString(text, 0, height - 2);
		g.dispose();

		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			ImageIO.write(image, "PNG", output);
			byte[] b = output.toByteArray();
			return Base64.getEncoder().encodeToString(b);
		} catch (Exception e) { }
		return "";
	}
	
	static String randomPhotoCode() {
		int r = (int)(Math.random() * 10000);
		String s = "" + r;
		if (s.length() < 4) s = "0" + s;
		if (s.length() < 4) s = "0" + s;
		if (s.length() < 4) s = "0" + s;
		return s;
	}
}