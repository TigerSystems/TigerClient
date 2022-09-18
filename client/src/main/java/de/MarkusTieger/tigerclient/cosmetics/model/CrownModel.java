package de.MarkusTieger.tigerclient.cosmetics.model;

import net.minecraft.world.entity.Entity;

public class CrownModel<T extends Entity> /* extends EntityModel<T> */ {
	/*
	 * public ModelPart crown1; public ModelPart crown2; public ModelPart crown3;
	 * public ModelPart crown4; public ModelPart crown5; public ModelPart crown6;
	 * public ModelPart crown7; public ModelPart crown8; public ModelPart
	 * crownside1; public ModelPart crownside2; public ModelPart kreutz1; public
	 * ModelPart kreutz2; public ModelPart crown1_1; public ModelPart crown2_1;
	 * public ModelPart crown3_1; public ModelPart crown4_1; public ModelPart
	 * crown5_1; public ModelPart crown6_1; public ModelPart crown7_1; public
	 * ModelPart crown8_1; public ModelPart crownside1_1; public ModelPart
	 * crownside2_1; public ModelPart kreutz1_1; public ModelPart kreutz2_1; public
	 * ModelPart crown2_2; public ModelPart crown3_2; public ModelPart crown5_2;
	 * public ModelPart crown6_2; public ModelPart crown7_2; public ModelPart
	 * crown8_2; public ModelPart crownside1_2; public ModelPart crownside2_2;
	 * public ModelPart kreutz1_2; public ModelPart kreutz2_2; public ModelPart
	 * crown2_3; public ModelPart crown3_3; public ModelPart crown5_3; public
	 * ModelPart crown6_3; public ModelPart crown7_3; public ModelPart crown8_3;
	 * public ModelPart crownside1_3; public ModelPart crownside2_3; public
	 * ModelPart kreutz1_3; public ModelPart kreutz2_3; public ModelPart dia1;
	 * public ModelPart dia2; public ModelPart dia3; public ModelPart dia1_1; public
	 * ModelPart dia2_1; public ModelPart dia3_1; public ModelPart dia1_2; public
	 * ModelPart dia2_2; public ModelPart dia3_2; public ModelPart dia1_3; public
	 * ModelPart dia2_3; public ModelPart dia3_3; public ModelPart cube; public
	 * PlayerModel<AbstractClientPlayerEntity> model;
	 * 
	 * public CrownModel(PlayerModel<AbstractClientPlayerEntity> model) { this.model
	 * = model; this.textureWidth = 64; this.textureHeight = 64; this.crown2 = new
	 * ModelPart(this, 0, 0); this.crown2.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crown2.addBox(3.1F, -13.4F, -4.8F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F); this.cube = new ModelPart(this, 0, 0); this.cube.setRotation(0.0F,
	 * 0.0F, 0.0F); this.cube.addBox(-4.0F, -11.5F, -4.0F, 8.0F, 5.0F, 8.0F, 0.5F,
	 * 0.5F, 0.5F); this.crown8 = new ModelPart(this, 0, 0);
	 * this.crown8.setRotation(0.0F, 0.0F, 0.0F); this.crown8.addBox(1.0F, -12.9F,
	 * -4.8F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F, -0.2F); this.kreutz2_2 = new
	 * ModelPart(this, 0, 0); this.kreutz2_2.setRotation(0.0F, 0.0F, 0.0F);
	 * this.kreutz2_2.addBox(-4.8F, -15.9F, -1.0F, 1.0F, 1.0F, 2.0F, -0.2F, -0.2F,
	 * -0.2F); this.crown4 = new ModelPart(this, 0, 0);
	 * this.crown4.setRotation(0.0F, 0.0F, 0.0F); this.crown4.addBox(-4.7F, -14.5F,
	 * -4.8F, 1.0F, 3.0F, 1.0F, -0.2F, -0.2F, -0.2F); this.crown8_2 = new
	 * ModelPart(this, 0, 0); this.crown8_2.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crown8_2.addBox(-4.8F, -12.9F, -1.9F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F); this.crown5_1 = new ModelPart(this, 0, 0);
	 * this.crown5_1.setRotation(0.0F, 0.0F, 0.0F); this.crown5_1.addBox(-4.1F,
	 * -13.4F, 3.7F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F, -0.2F); this.crown2_3 = new
	 * ModelPart(this, 0, 0); this.crown2_3.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crown2_3.addBox(3.8F, -13.4F, -4.2F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F); this.crown2_2 = new ModelPart(this, 0, 0);
	 * this.crown2_2.setRotation(0.0F, 0.0F, 0.0F); this.crown2_2.addBox(-4.8F,
	 * -13.4F, -4.2F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F, -0.2F); this.dia1_2 = new
	 * ModelPart(this, 0, 48); this.dia1_2.setRotation(0.0F, 0.0F, 0.0F);
	 * this.dia1_2.addBox(4.0F, -11.6F, -1.0F, 1.0F, 2.0F, 2.0F, -0.2F, -0.2F,
	 * -0.2F); this.crown5 = new ModelPart(this, 0, 0);
	 * this.crown5.setRotation(0.0F, 0.0F, 0.0F); this.crown5.addBox(-4.1F, -13.4F,
	 * -4.8F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F, -0.2F); this.kreutz1_3 = new
	 * ModelPart(this, 0, 0); this.kreutz1_3.setRotation(0.0F, 0.0F, 0.0F);
	 * this.kreutz1_3.addBox(3.8F, -16.5F, -0.5F, 1.0F, 3.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F); this.crown6_3 = new ModelPart(this, 0, 0);
	 * this.crown6_3.setRotation(0.0F, 0.0F, 0.0F); this.crown6_3.ad(3.9F, -12.9F,
	 * 2.5F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F, -0.2F); this.crown7_3 = new
	 * ModelPart(this, 0, 0); this.crown7_3.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crown7_3.addBox(3.9F, -12.9F, 1.2F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F); this.kreutz1 = new ModelPart(this, 0, 0);
	 * this.kreutz1.setRotation(0.0F, 0.0F, 0.0F); this.kreutz1.addBox(-0.5F,
	 * -16.5F, -5.0F, 1.0F, 3.0F, 1.0F, -0.3F, -0.3F, -0.3F); this.dia3 = new
	 * ModelPart(this, 0, 48); this.dia3.setRotation(0.0F, 0.0F, 0.0F);
	 * this.dia3.addBox(1.5F, -11.1F, 4.0F, 2.0F, 2.0F, 1.0F, -0.4F, -0.4F, -0.4F);
	 * this.crownside2 = new ModelPart(this, 0, 0);
	 * this.crownside2.setRotation(0.0F, 0.0F, 0.0F); this.crownside2.addBox(-1.0F,
	 * -14.5F, -4.8F, 2.0F, 2.0F, 1.0F, -0.2F, -0.2F, -0.2F); this.kreutz2_1 = new
	 * ModelPart(this, 0, 0); this.kreutz2_1.setRotation(0.0F, 0.0F, 0.0F);
	 * this.kreutz2_1.addBox(-1.0F, -15.9F, 3.5F, 2.0F, 1.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F); this.crown7_1 = new ModelPart(this, 0, 0);
	 * this.crown7_1.setRotation(0.0F, 0.0F, 0.0F); this.crown7_1.addBox(-2.1F,
	 * -12.9F, 3.7F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F, -0.2F); this.crown8_3 = new
	 * ModelPart(this, 0, 0); this.crown8_3.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crown8_3.addBox(3.8F, -12.9F, -1.9F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F); this.crownside2_3 = new ModelPart(this, 0, 0);
	 * this.crownside2_3.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crownside2_3.addBox(3.8F, -14.5F, -0.9F, 1.0F, 2.0F, 2.0F, -0.2F, -0.2F,
	 * -0.2F); this.crownside2_1 = new ModelPart(this, 0, 0);
	 * this.crownside2_1.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crownside2_1.addBox(-1.0F, -14.5F, 3.7F, 2.0F, 2.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F); this.dia2_3 = new ModelPart(this, 0, 48);
	 * this.dia2_3.setRotation(0.0F, 0.0F, 0.0F); this.dia2_3.addBox(-5.0F, -11.1F,
	 * 1.5F, 1.0F, 2.0F, 2.0F, -0.4F, -0.4F, -0.4F); this.crownside1 = new
	 * ModelPart(this, 0, 0); this.crownside1.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crownside1.addBox(-1.5F, -12.9F, -4.8F, 3.0F, 2.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F); this.crownside1_3 = new ModelPart(this, 0, 0);
	 * this.crownside1_3.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crownside1_3.addBox(3.8F, -12.9F, -1.4F, 1.0F, 2.0F, 3.0F, -0.2F, -0.2F,
	 * -0.2F); this.dia1_1 = new ModelPart(this, 0, 48);
	 * this.dia1_1.setRotation(0.0F, 0.0F, 0.0F); this.dia1_1.addBox(-1.0F, -11.6F,
	 * -5.0F, 2.0F, 2.0F, 1.0F, -0.2F, -0.2F, -0.2F); this.crown6_1 = new
	 * ModelPart(this, 0, 0); this.crown6_1.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crown6_1.addBox(-3.5F, -12.9F, 3.7F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F); this.crown7_2 = new ModelPart(this, 0, 0);
	 * this.crown7_2.setRotation(0.0F, 0.0F, 0.0F); this.crown7_2.addBox(-4.7F,
	 * -12.9F, 1.2F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F, -0.2F); this.crownside1_1 = new
	 * ModelPart(this, 0, 0); this.crownside1_1.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crownside1_1.addBox(-1.5F, -12.9F, 3.7F, 3.0F, 2.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F); this.dia3_1 = new ModelPart(this, 0, 48);
	 * this.dia3_1.setRotation(0.0F, 0.0F, 0.0F); this.dia3_1.addBox(1.5F, -11.1F,
	 * -5.0F, 2.0F, 2.0F, 1.0F, -0.4F, -0.4F, -0.4F); this.crown5_2 = new
	 * ModelPart(this, 0, 0); this.crown5_2.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crown5_2.addBox(-4.7F, -13.4F, 3.1F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F); this.kreutz1_1 = new ModelPart(this, 0, 0);
	 * this.kreutz1_1.setRotation(0.0F, 0.0F, 0.0F); this.kreutz1_1.addBox(-0.5F,
	 * -16.5F, 3.5F, 1.0F, 3.0F, 1.0F, -0.2F, -0.2F, -0.2F); this.crown3_3 = new
	 * ModelPart(this, 0, 0); this.crown3_3.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crown3_3.addBox(3.8F, -12.9F, -3.6F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F); this.dia3_2 = new ModelPart(this, 0, 48);
	 * this.dia3_2.setRotation(0.0F, 0.0F, 0.0F); this.dia3_2.addBox(4.0F, -11.1F,
	 * -3.5F, 1.0F, 2.0F, 2.0F, -0.4F, -0.4F, -0.4F); this.crown6 = new
	 * ModelPart(this, 0, 0); this.crown6.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crown6.addBox(-3.5F, -12.9F, -4.8F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F); this.crown1 = new ModelPart(this, 0, 0);
	 * this.crown1.setRotation(0.0F, 0.0F, 0.0F); this.crown1.addBox(3.7F, -14.5F,
	 * -4.8F, 1.0F, 3.0F, 1.0F, -0.2F, -0.2F, -0.2F); this.crownside1_2 = new
	 * ModelPart(this, 0, 0); this.crownside1_2.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crownside1_2.addBox(-4.8F, -12.9F, -1.4F, 1.0F, 2.0F, 3.0F, -0.2F,
	 * -0.2F, -0.2F); this.crown6_2 = new ModelPart(this, 0, 0);
	 * this.crown6_2.setRotation(0.0F, 0.0F, 0.0F); this.crown6_2.addBox(-4.7F,
	 * -12.9F, 2.5F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F, -0.2F); this.crown5_3 = new
	 * ModelPart(this, 0, 0); this.crown5_3.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crown5_3.addBox(3.9F, -13.4F, 3.1F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F); this.crown7 = new ModelPart(this, 0, 0);
	 * this.crown7.setRotation(0.0F, 0.0F, 0.0F); this.crown7.addBox(-2.1F, -12.9F,
	 * -4.8F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F, -0.2F); this.crown3_2 = new
	 * ModelPart(this, 0, 0); this.crown3_2.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crown3_2.addBox(-4.8F, -12.9F, -3.6F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F); this.kreutz1_2 = new ModelPart(this, 0, 0);
	 * this.kreutz1_2.setRotation(0.0F, 0.0F, 0.0F); this.kreutz1_2.addBox(-4.8F,
	 * -16.5F, -0.5F, 1.0F, 3.0F, 1.0F, -0.2F, -0.2F, -0.2F); this.dia2 = new
	 * ModelPart(this, 0, 48); this.dia2.setRotation(0.0F, 0.0F, 0.0F);
	 * this.dia2.addBox(-3.5F, -11.1F, 4.0F, 2.0F, 2.0F, 1.0F, -0.4F, -0.4F, -0.4F);
	 * this.dia2_2 = new ModelPart(this, 0, 48); this.dia2_2.setRotation(0.0F, 0.0F,
	 * 0.0F); this.dia2_2.addBox(4.0F, -11.1F, 1.5F, 1.0F, 2.0F, 2.0F, -0.4F, -0.4F,
	 * -0.4F); this.dia3_3 = new ModelPart(this, 0, 48);
	 * this.dia3_3.setRotation(0.0F, 0.0F, 0.0F); this.dia3_3.addBox(-5.0F, -11.1F,
	 * -3.5F, 1.0F, 2.0F, 2.0F, -0.4F, -0.4F, -0.4F); this.crownside2_2 = new
	 * ModelPart(this, 0, 0); this.crownside2_2.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crownside2_2.addBox(-4.8F, -14.5F, -0.9F, 1.0F, 2.0F, 2.0F, -0.2F,
	 * -0.2F, -0.2F); this.dia1_3 = new ModelPart(this, 0, 48);
	 * this.dia1_3.setRotation(0.0F, 0.0F, 0.0F); this.dia1_3.addBox(-5.0F, -11.6F,
	 * -1.0F, 1.0F, 2.0F, 2.0F, -0.2F, -0.2F, -0.2F); this.crown4_1 = new
	 * ModelPart(this, 0, 0); this.crown4_1.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crown4_1.addBox(-4.7F, -14.5F, 3.7F, 1.0F, 3.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F); this.crown1_1 = new ModelPart(this, 0, 0);
	 * this.crown1_1.setRotation(0.0F, 0.0F, 0.0F); this.crown1_1.addBox(3.7F,
	 * -14.5F, 3.7F, 1.0F, 3.0F, 1.0F, -0.2F, -0.2F, -0.2F); this.crown3 = new
	 * ModelPart(this, 0, 0); this.crown3.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crown3.addBox(2.5F, -12.9F, -4.8F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F); this.kreutz2_3 = new ModelPart(this, 0, 0);
	 * this.kreutz2_3.setRotation(0.0F, 0.0F, 0.0F); this.kreutz2_3.addBox(3.8F,
	 * -15.9F, -1.0F, 1.0F, 1.0F, 2.0F, -0.2F, -0.2F, -0.2F); this.kreutz2 = new
	 * ModelPart(this, 0, 0); this.kreutz2.setRotation(0.0F, 0.0F, 0.0F);
	 * this.kreutz2.addBox(-1.0F, -15.9F, -5.0F, 2.0F, 1.0F, 1.0F, -0.3F, -0.3F,
	 * -0.3F); this.crown3_1 = new ModelPart(this, 0, 0);
	 * this.crown3_1.setRotation(0.0F, 0.0F, 0.0F); this.crown3_1.addBox(2.5F,
	 * -12.9F, 3.7F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F, -0.2F); this.dia1 = new
	 * ModelPart(this, 0, 48); this.dia1.setRotation(0.0F, 0.0F, 0.0F);
	 * this.dia1.addBox(-1.0F, -11.6F, 4.0F, 2.0F, 2.0F, 1.0F, -0.2F, -0.2F, -0.2F);
	 * this.dia2_1 = new ModelPart(this, 0, 48); this.dia2_1.setRotation(0.0F, 0.0F,
	 * 0.0F); this.dia2_1.addBox(-3.5F, -11.1F, -5.0F, 2.0F, 2.0F, 1.0F, -0.4F,
	 * -0.4F, -0.4F); this.crown8_1 = new ModelPart(this, 0, 0);
	 * this.crown8_1.setRotation(0.0F, 0.0F, 0.0F); this.crown8_1.addBox(1.0F,
	 * -12.9F, 3.7F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F, -0.2F); this.crown2_1 = new
	 * ModelPart(this, 0, 0); this.crown2_1.setRotation(0.0F, 0.0F, 0.0F);
	 * this.crown2_1.addBox(3.1F, -13.4F, 3.7F, 1.0F, 2.0F, 1.0F, -0.2F, -0.2F,
	 * -0.2F);
	 * 
	 * ImmutableList.of(this.crown2, this.cube, this.crown8, this.kreutz2_2,
	 * this.crown4, this.crown8_2, this.crown5_1, this.crown2_3, this.crown2_2,
	 * this.crown5, this.kreutz1_3, this.crown6_3, this.crown7_3, this.kreutz1,
	 * this.crownside2, this.kreutz2_1, this.crown7_1, this.crown8_3,
	 * this.crownside2_3, this.crownside2_1, this.crownside1, this.crownside1_3,
	 * this.crown6_1, this.crown7_2, this.crownside1_1, this.crown5_2,
	 * this.kreutz1_1, this.crown3_3, this.crown6, this.crown1, this.crownside1_2,
	 * this.crown6_2, this.crown5_3, this.crown7, this.crown3_2, this.kreutz1_2,
	 * this.crownside2_2, this.crown4_1, this.crown1_1, this.crown3, this.kreutz2_3,
	 * this.kreutz2, this.crown3_1, this.crown8_1, this.crown2_1, this.dia1, dia1_1,
	 * dia1_2, dia1_3, dia2, dia2_1, dia2_2, dia2_3, dia3, dia3_1, dia3_2, dia3_3)
	 * .forEach((ModelPart) -> { ModelPart.setTextureSize(64, 64); });
	 * 
	 * }
	 * 
	 * @Override public void render(PoseStack PoseStackIn, IVertexBuilder bufferIn,
	 * int packedLightIn, int packedOverlayIn, float red, float green, float blue,
	 * float alpha) { ImmutableList.of(this.crown2, this.cube, this.crown8,
	 * this.kreutz2_2, this.crown4, this.crown8_2, this.crown5_1, this.crown2_3,
	 * this.crown2_2, this.crown5, this.kreutz1_3, this.crown6_3, this.crown7_3,
	 * this.kreutz1, this.crownside2, this.kreutz2_1, this.crown7_1, this.crown8_3,
	 * this.crownside2_3, this.crownside2_1, this.crownside1, this.crownside1_3,
	 * this.crown6_1, this.crown7_2, this.crownside1_1, this.crown5_2,
	 * this.kreutz1_1, this.crown3_3, this.crown6, this.crown1, this.crownside1_2,
	 * this.crown6_2, this.crown5_3, this.crown7, this.crown3_2, this.kreutz1_2,
	 * this.crownside2_2, this.crown4_1, this.crown1_1, this.crown3, this.kreutz2_3,
	 * this.kreutz2, this.crown3_1, this.crown8_1, this.crown2_1, this.dia1, dia1_1,
	 * dia1_2, dia1_3, dia2, dia2_1, dia2_2, dia2_3, dia3, dia3_1, dia3_2, dia3_3)
	 * .forEach((ModelPart) -> { ModelPart.rotateAngleX =
	 * model.bipedHead.rotateAngleX; ModelPart.rotateAngleY =
	 * model.bipedHead.rotateAngleY; ModelPart.rotateAngleZ =
	 * model.bipedHead.rotateAngleZ; }); ImmutableList.of(this.crown2, this.cube,
	 * this.crown8, this.kreutz2_2, this.crown4, this.crown8_2, this.crown5_1,
	 * this.crown2_3, this.crown2_2, this.crown5, this.kreutz1_3, this.crown6_3,
	 * this.crown7_3, this.kreutz1, this.crownside2, this.kreutz2_1, this.crown7_1,
	 * this.crown8_3, this.crownside2_3, this.crownside2_1, this.crownside1,
	 * this.crownside1_3, this.crown6_1, this.crown7_2, this.crownside1_1,
	 * this.crown5_2, this.kreutz1_1, this.crown3_3, this.crown6, this.crown1,
	 * this.crownside1_2, this.crown6_2, this.crown5_3, this.crown7, this.crown3_2,
	 * this.kreutz1_2, this.crownside2_2, this.crown4_1, this.crown1_1, this.crown3,
	 * this.kreutz2_3, this.kreutz2, this.crown3_1, this.crown8_1, this.crown2_1)
	 * .forEach((ModelPart) -> { ModelPart.render(PoseStackIn, bufferIn,
	 * packedLightIn, packedOverlayIn, 255, 255, 255, alpha); });
	 * ImmutableList.of(this.dia1, dia1_1, dia1_2, dia1_3, dia2, dia2_1, dia2_2,
	 * dia2_3, dia3, dia3_1, dia3_2, dia3_3) .forEach((ModelPart) -> {
	 * ModelPart.render(PoseStackIn, bufferIn, packedLightIn, packedOverlayIn, red,
	 * green, blue, alpha); }); }
	 * 
	 * public void setRotateAngle(ModelPart ModelPart, float x, float y, float z) {
	 * ModelPart.rotateAngleX = x; ModelPart.rotateAngleY = y;
	 * ModelPart.rotateAngleZ = z; }
	 * 
	 * @Override public void setRotationAngles(T entityIn, float limbSwing, float
	 * limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) { }
	 */
}
