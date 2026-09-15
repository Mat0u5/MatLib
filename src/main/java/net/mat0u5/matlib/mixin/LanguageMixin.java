package net.mat0u5.matlib.mixin;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.events.server.ServerLanguageEvents;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mixin(Language.class)
public class LanguageMixin {

	@Inject(method = "loadDefault", at = @At("RETURN"), cancellable = true)
	private static void onServerLanguageLoad(CallbackInfoReturnable<Language> cir) {
		//if (.hasClient()) return;//TODO
		Language vanilla = cir.getReturnValue();
		Map<String, String> customTranslations = new HashMap<>();

		List<String> fileNames = ServerLanguageEvents.LOAD_LANG_FILES.invoker().getPaths();
		for (String fileName : fileNames) {
			try (InputStream is = LanguageMixin.class.getResourceAsStream(fileName)) {
				if (is != null) {
					Language.loadFromJson(is, customTranslations::put);
				}
			} catch (Exception e) {
				MatLib.LOGGER.error("Failed to load server-side translation: {}", fileName);
				e.printStackTrace();
			}
		}

		cir.setReturnValue(new Language() {
			@Override
			public String getOrDefault(String key, String fallback) {
				if (customTranslations.containsKey(key)) {
					return customTranslations.get(key);
				}
				return vanilla.getOrDefault(key, fallback);
			}

			@Override
			public boolean has(String key) {
				return customTranslations.containsKey(key) || vanilla.has(key);
			}

			@Override
			public boolean isDefaultRightToLeft() {
				return vanilla.isDefaultRightToLeft();
			}

			@Override
			public FormattedCharSequence getVisualOrder(FormattedText text) {
				return vanilla.getVisualOrder(text);
			}
		});
	}
}