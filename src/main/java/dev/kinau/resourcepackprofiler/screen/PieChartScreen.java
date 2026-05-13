package dev.kinau.resourcepackprofiler.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.profiling.ProfileResults;
import net.minecraft.util.profiling.ResultField;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class PieChartScreen extends Screen {

    private final ProfileResults results;

    public PieChartScreen(ProfileResults results) {
        super(Component.translatable("resourcePackProfiler.screen.title"));
        this.results = results;
    }


    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        super.render(guiGraphics, i, j, f);
        renderPie(guiGraphics, results);
    }

    private void renderPie(GuiGraphics guiGraphics, ProfileResults profileResults) {
        Objects.requireNonNull(this.font);

        List<ResultField> list = new ArrayList<>(profileResults.getTimes("root"));
        int resultCount = list.size();

        int midX = guiGraphics.guiWidth() / 2;
        int startX = midX - 135;
        int endX = midX + 135;

        int height = resultCount * 9;
        int extraHeight = 62;
        int endY = guiGraphics.guiHeight() / 2 + height / 5 + extraHeight;
        int startY = endY - height - extraHeight;

        guiGraphics.fill(startX - 5, startY - 5, endX + 5, endY + 5 + extraHeight, -1873784752);
        int pieStartY = startY + extraHeight;

        guiGraphics.submitProfilerChartRenderState(list, startX, pieStartY, endX, endY);

        DecimalFormat decimalFormat = new DecimalFormat("##0.00");
        decimalFormat.setDecimalFormatSymbols(DecimalFormatSymbols.getInstance(Locale.ROOT));
        guiGraphics.drawCenteredString(this.font, this.title, midX, pieStartY - 80, 0xFFFFFF);
        int maxMsTextWidth = list.stream().mapToInt(value -> this.font.width(value.count + "ms")).max().orElse(0);
        for (int i = 0; i < list.size(); i++) {
            ResultField resultField3 = list.get(i);
            String text = resultField3.name;
            guiGraphics.drawString(this.font, text, startX, startY + extraHeight * 2 + i * 9, resultField3.getColor());
            String percentText = decimalFormat.format(resultField3.percentage) + "%";
            String msText = resultField3.count + "ms";

            guiGraphics.drawString(this.font, percentText, endX - maxMsTextWidth - 3 - this.font.width(percentText), startY + extraHeight * 2 + i * 9, resultField3.getColor());
            guiGraphics.drawString(this.font, msText, endX - this.font.width(msText), startY + extraHeight * 2 + i * 9, resultField3.getColor());
        }
    }
}
