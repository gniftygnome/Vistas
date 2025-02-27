package com.terraformersmc.vistas.panorama;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.client.sound.MusicType;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class Panorama {

	public static final Panorama DEFAULT = new Panorama();

	public static final Codec<MusicSound> OPTIONAL_MUSIC_SOUND_CODEC = RecordCodecBuilder.create(
			(instance) -> 
			instance.group(
					SoundEvent.CODEC.optionalFieldOf("sound")
						.forGetter((sound) -> Optional.of(sound.getSound())),
					Codec.INT.optionalFieldOf("min_delay")
						.forGetter((sound) -> Optional.of(sound.getMinDelay())),
					Codec.INT.optionalFieldOf("max_delay")
						.forGetter((sound) -> Optional.of(sound.getMaxDelay())),
					Codec.BOOL.optionalFieldOf("replace_current_music")
						.forGetter((sound) -> Optional.of(sound.shouldReplaceCurrentMusic()))
					)
			.apply(instance, (sound, min, max, replace)-> new MusicSound(sound.orElse(SoundEvents.MUSIC_MENU), min.orElse(20), max.orElse(600), replace.orElse(true))));

	public static final Codec<Panorama> CODEC = RecordCodecBuilder.create(
			(instance) -> 
			instance.group(
					Codec.INT.optionalFieldOf("weight")
						.forGetter((panorama) -> Optional.of(panorama.weight)),
					OPTIONAL_MUSIC_SOUND_CODEC.optionalFieldOf("musicSound")
						.forGetter((panorama) -> Optional.of(panorama.musicSound)),
					Identifier.CODEC.optionalFieldOf("splashText")
						.forGetter((panorama) -> Optional.of(panorama.splashText)),
					LogoControl.CODEC.optionalFieldOf("logoControl")
						.forGetter((panorama) -> Optional.of(panorama.logoControl)),
					Codec.list(Cubemap.CODEC).optionalFieldOf("cubemaps")
						.forGetter((panorama) -> Optional.of(panorama.cubemaps))
					)
			.apply(instance, Panorama::new));

	private final int weight;
	private final MusicSound musicSound;
	private final Identifier splashText;
	private final LogoControl logoControl;
	private final List<Cubemap> cubemaps;

	public Panorama() {
		this.weight = 1;
		this.musicSound = MusicType.MENU;
		this.splashText = new Identifier("texts/splashes.txt");
		this.logoControl = LogoControl.DEFAULT;
		this.cubemaps = Lists.newArrayList(Cubemap.DEFAULT);
	}

	public Panorama(int weight, MusicSound musicSound, Identifier splashText, LogoControl logoControl, List<Cubemap> cubemaps) {
		this.weight = weight;
		this.musicSound = musicSound;
		this.splashText = splashText;
		this.logoControl = logoControl;
		this.cubemaps = cubemaps;
	}

	public Panorama(Optional<Integer> weight, Optional<MusicSound> musicSound, Optional<Identifier> splashText, Optional<LogoControl> logoControl, Optional<List<Cubemap>> cubemaps) {
		this.weight = weight.orElse(1);
		this.musicSound = musicSound.orElse(MusicType.MENU);
		this.splashText = splashText.orElse(new Identifier("texts/splashes.txt"));
		this.logoControl = logoControl.orElse(LogoControl.DEFAULT);
		this.cubemaps = cubemaps.orElse(Lists.newArrayList(Cubemap.DEFAULT));
	}

	public int getWeight() {
		return weight;
	}

	public MusicSound getMusicSound() {
		return musicSound;
	}

	public Identifier getSplashText() {
		return splashText;
	}

	public LogoControl getLogoControl() {
		return logoControl;
	}

	public List<Cubemap> getCubemaps() {
		return cubemaps;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Panorama pan) {
			return this.weight == pan.weight && this.musicSound == pan.musicSound && this.splashText == pan.splashText && this.logoControl == pan.logoControl && this.cubemaps == pan.cubemaps;
		}
		return super.equals(obj);
	}

}
