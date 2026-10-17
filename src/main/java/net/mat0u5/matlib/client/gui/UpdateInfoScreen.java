package net.mat0u5.matlib.client.gui;

import net.mat0u5.matlib.client.config.MatLibClientData;
import net.mat0u5.matlib.client.render.RenderUtils;
import net.mat0u5.matlib.client.utils.ClientUtils;
import net.mat0u5.matlib.client.utils.TextColors;
import net.mat0u5.matlib.services.VersionTrackedMod;
import net.mat0u5.matlib.utils.other.TextUtils;
import net.mat0u5.matlib.utils.other.UpdateChecker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class UpdateInfoScreen extends DefaultScreen {
    private final UpdateChecker.UpdateInfo info;
    private final String description;
    private final Component dismissText = Component.nullToEmpty("Dismiss for this update");
    private int textWidth = 0;
    private Button dismissButton;

    public UpdateInfoScreen(UpdateChecker.UpdateInfo info) {
        super(Component.nullToEmpty("New " + info.mod.modReadableName() + " Update"), 400, 225, 0, +10);
        this.info = info;
        this.description = info.versionDescription.replace("\r", "");
    }

    public boolean isInCheckboxRegion(int x, int y) {
        return (x >= endX - textWidth/2-2-40) && (x <= endX + textWidth/2+1-40)
                && y >= startY - 23 && y <= startY + 3;
    }

    @Override
    protected void init() {
        super.init();
        VersionTrackedMod mod = info.mod;
        textWidth = font.width(dismissText) + 5;

        String discordUrl = mod.discordUrl();
        if (discordUrl != null) {
            this.addRenderableWidget(
                    Button.builder(Component.literal("Join Discord").withStyle(style -> style.withColor(TextColors.PASTEL_WHITE)), btn -> {
                                ClientUtils.openExternalLink(discordUrl);
                            })
                            .pos(startX + 5, endY - 25)
                            .size(80, 20)
                            .build()
            );
        }
        String changelogLink = info.getChangelogLink();
        if (changelogLink != null) {
            this.addRenderableWidget(
                    Button.builder(Component.literal("Full Changelog").withStyle(style -> style.withColor(TextColors.PASTEL_WHITE)), btn -> {
                                ClientUtils.openExternalLink(changelogLink);
                            })
                            .pos(endX - 80 - 5, endY - 25)
                            .size(80, 20)
                            .build()
            );
        }
        String modrinthUrl = mod.modrinthUrl();
        if (modrinthUrl != null) {
            this.addRenderableWidget(
                    Button.builder(Component.literal("Download on Modrinth"), btn -> {
                                this.onClose();
                                ClientUtils.openExternalLink(modrinthUrl); //Same as having a text with a click event, but that doesnt work in GUIs
                            })
                            .pos(centerX - 85, endY - 25)
                            .size(170, 20)
                            .build()
            );
        }
        dismissButton = this.addRenderableWidget(
                Button.builder(dismissText, btn -> {
                            MatLibClientData.get().dismissUpdate(mod.modId(), info.version);
                            this.onClose();
                        })
                        .pos(endX - textWidth/2-40, startY - 20)
                        .size(textWidth, 16)
                        .build()
        );
    }

    @Override
    public void renderBackground(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        if (isInCheckboxRegion(mouseX, mouseY)) {
            context.fill(endX - textWidth/2-3-40, startY - 23, endX + textWidth/2+3-40, startY, TextColors.BLACK);
            context.fill(endX - textWidth/2-2-40, startY - 22, endX + textWidth/2+2-40, startY - 1, TextColors.GUI_BACKGROUND);
            dismissButton.visible = true;
        }
        else {
            dismissButton.visible = false;
        }
        super.renderBackground(context, mouseX, mouseY);
    }

    @Override
    public void render(GuiGraphicsExtractor context, int mouseX, int mouseY) {
        RenderUtils.text(TextUtils.formatString("§0§nA new {} mod update is available!", info.mod.modReadableName()), centerX, startY + 7).anchorCenter().render(context, this.font);
        RenderUtils.text(TextUtils.formatLoosely("§0§nChangelog in version §l{}§0:", info.versionName), startX + 7, startY + 25 + font.lineHeight).render(context, this.font);
        RenderUtils.text(description, startX + 7, startY + 30 + font.lineHeight*2).wrapLines(BG_WIDTH-14, 5).render(context, this.font);
    }
}
