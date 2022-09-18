package de.MarkusTieger.tac.gui.widgets.connect;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import de.MarkusTieger.common.gui.widgets.connect.renderrer.IConnectRenderer;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class TACConnectWidget extends GuiComponent implements Widget, GuiEventListener {

	public int y;
	public final Screen sc;
	private final ArrayList<IConnectRenderer> renderers = new ArrayList<>();

	public TACConnectWidget(Screen sc, int y) {
		this.sc = sc;
		this.y = y;
	}

	public void updateSize(int y) {
		this.y = y;
	}

	public void addRenderer(IConnectRenderer renderer) {
		this.renderers.add(renderer);
	}

	@Override
	public void render(PoseStack stack, int p_94670_, int p_94671_, float p_94672_) {

		double multiplicator = (((((sc.width) / 8D) * 4D) / 1024D));

		img(multiplicator, sc.width / 4, y - (int) (512 * multiplicator), (sc.width / 8) * 6, y, (funcX, funcY) -> {

			// int basic = image.getHeight() / 8;
			//
			// g.drawImage(mc, basic, image.getHeight() - ((basic * 2) + (basic)), basic *
			// 2, basic * 2, null);
			//
			// g.drawImage(internet, (int)(basic * 4.85), image.getHeight() - ((((basic *
			// 2))) + (basic)), ((basic * 2)), ((basic * 2)), null);
			//
			// g.drawImage(mojang, (image.getWidth() / 2) - (mojang.getWidth() / 8), basic,
			// mojang.getWidth() / 2, mojang.getHeight() / 2, null);
			//
			// g.drawImage(server, image.getWidth() - (basic * 3), image.getHeight() -
			// ((basic * 2) + (basic)), basic * 2, basic * 2, null);

			// hline(stack, this.width / 4, y - (int)(512 * multiplicator), funcX, funcY,
			// mcPoint.x, mcPoint.y, itPoint.x, 0xFF454545);
			// vline(stack, this.width / 4, y - (int)(512 * multiplicator), funcX, funcY,
			// itPoint.x, itPoint.y, itmjConnect.y, 0xFF454545);
			// hline(stack, this.width / 4, y - (int)(512 * multiplicator), funcX, funcY,
			// itmjConnect.x, itmjConnect.y, mjPoint.x, 0xFF454545);

			// hline(stack, this.width / 4, y - (int)(512 * multiplicator), funcX, funcY,
			// itPoint.x, itPoint.y, svPoint.x, svCon ? 0xFFFFFFFF : 0xFF454545);

			// img(stack, this.width / 4, y - (int)(512 * multiplicator), svCon ?
			// LINE_ENABLED : LINE_DISABLED, funcX, funcY, itPoint.x, itPoint.y - 5,
			// svPoint.x - itPoint.x, itPoint.y + 5);

			// line(stack, this.width / 4, y - (int)(512 * multiplicator), funcX, funcY,
			// mcPoint.x, mcPoint.y, itPoint.x, itPoint.y, 0x454545);
			// line(stack, this.width / 4, y - (int)(512 * multiplicator), funcX, funcY,
			// itPoint.x, itPoint.y, itmjConnect.x, itmjConnect.y, 0x454545);
			// line(stack, this.width / 4, y - (int)(512 * multiplicator), funcX, funcY,
			// itmjConnect.x, itmjConnect.y, mjPoint.x, mjPoint.y, 0x454545);
			// line(stack, this.width / 4, y - (int)(512 * multiplicator), funcX, funcY,
			// itPoint.x, itPoint.y, svPoint.x, svPoint.y, 0x454545);

			// if(svCon) fillPoint(stack, this.width / 4, y - (int)(512 * multiplicator),
			// funcX, funcY, itPoint.x + svConTick, svPoint.y, 0xFFFFFFFF);

			for (IConnectRenderer renderer : renderers) {

				renderer.render(stack, multiplicator, funcX, funcY, sc.width / 4, y - (int) (512 * multiplicator));

			}

		});

	}

	public void fillPoint(PoseStack stack, int xImg, int yImg, Function<Integer, Integer> funcX,
			Function<Integer, Integer> funcY, int x, int y, int color) {
		GuiComponent.fill(stack, (funcX.apply(x) + xImg) - 2, (funcY.apply(y) + yImg) - 2, (funcX.apply(x) + xImg) + 2,
				(funcY.apply(y) + yImg) + 2, color);
	}

	public void vline(PoseStack stack, int xImg, int yImg, Function<Integer, Integer> funcX,
			Function<Integer, Integer> funcY, int x1, int y1, int y2, int color) {
		vLine(stack, funcX.apply(x1) + xImg, funcY.apply(y1) + yImg, funcY.apply(y2) + yImg, color);
	}

	public void hline(PoseStack stack, int xImg, int yImg, Function<Integer, Integer> funcX,
			Function<Integer, Integer> funcY, int x1, int y1, int x2, int color) {
		hLine(stack, funcX.apply(x1) + xImg, funcX.apply(x2) + xImg, funcY.apply(y1) + yImg, color);
	}

	public void img(PoseStack stack, int xImg, int yImg, ResourceLocation resourceLocation,
			Function<Integer, Integer> funcX, Function<Integer, Integer> funcY, int x, int y, int width, int height) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, resourceLocation);

		blit(stack, funcX.apply(x) + xImg, funcY.apply(y) + yImg, 0F, 0F, funcX.apply(width), funcY.apply(height),
				funcX.apply(width), funcY.apply(height));

	}

	public void fill(PoseStack stack, int xImg, int yImg, Function<Integer, Integer> funcX,
			Function<Integer, Integer> funcY, int x1, int y1, int x2, int y2, int color) {
		fill(stack, funcX.apply(x1) + xImg, funcY.apply(y1) + yImg, funcX.apply(x2), funcY.apply(y2), color);
	}

	public void img(double multiplicator, int x1, int y1, int x2, int y2,
			BiConsumer<Function<Integer, Integer>, Function<Integer, Integer>> onFinish) {

		Function<Integer, Integer> funcX = (in) -> {
			int out = in;
			out *= multiplicator;
			return out;
		};
		Function<Integer, Integer> funcY = (in) -> {
			int out = in;
			out *= multiplicator;
			return out;
		};

		onFinish.accept(funcX, funcY);

	}

	public int getY() {
		double multiplicator = (((((sc.width) / 8D) * 4D) / 1024D));
		return y - ((int) (512 * multiplicator));
	}

	public int getHeight() {
		return y - getY();
	}
}
