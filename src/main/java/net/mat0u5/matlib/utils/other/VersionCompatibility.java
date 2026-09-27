package net.mat0u5.matlib.utils.other;

import net.mat0u5.matlib.MatLib;
import net.mat0u5.matlib.services.VersionTrackedMod;
import net.mat0u5.matlib.utils.interfaces.VersionTrackedModImpl;

public record VersionCompatibility(String minVersion, String maxVersion, String equalVersion) {
	public static class Result {
		public static Result PASS = new Result(null);
		private final String error;
		private Result(String error) {
			this.error = error;
		}

		public static Result error(String error) {
			return new Result(error);
		}

		public boolean passes() {
			return this.error == null;
		}
		public String getError() {
			return this.error;
		}
	}

	public static VersionCompatibility min(String minVersion) {
		return new VersionCompatibility(minVersion, null, null);
	}

	public static VersionCompatibility max(String maxVersion) {
		return new VersionCompatibility(null, maxVersion, null);
	}

	public static VersionCompatibility equal(String equalVersion) {
		return new VersionCompatibility(null, null, equalVersion);
	}

	public static VersionCompatibility any() {
		return new VersionCompatibility(null, null, null);
	}

	public boolean isAny() {
		return minVersion == null && maxVersion == null && equalVersion == null;
	}

	public static Result passes(VersionTrackedMod thisMod, VersionTrackedModImpl otherMod, boolean onServer) {
		if (!thisMod.modId().equalsIgnoreCase(otherMod.modId())) return Result.PASS;
		if (onServer) {
			return passesOnServer(thisMod.serverCompatibility(), otherMod);
		}
		else {
			return passesOnClient(thisMod.clientCompatibility(), otherMod);
		}
	}

	private static Result passesOnServer(VersionCompatibility serverSideCompat, VersionTrackedModImpl clientSideMod) {
		if (serverSideCompat.isAny()) return Result.PASS;
		int clientModVersionInt = getModVersionInt(clientSideMod.modVersion());
		Integer requiredMinVersionInt = getModVersionInt(serverSideCompat.minVersion);
		Integer requiredMaxVersionInt = getModVersionInt(serverSideCompat.maxVersion);
		if (requiredMinVersionInt == null) requiredMinVersionInt = -Integer.MAX_VALUE;
		if (requiredMaxVersionInt == null) requiredMaxVersionInt = Integer.MAX_VALUE;

		if (serverSideCompat.equalVersion != null && !serverSideCompat.equalVersion.equalsIgnoreCase(clientSideMod.modVersion())) {
			String error = TextUtils.formatString(
					"[{} Mod] Client-Server version mismatch!\n" +
							"You must join with version {}.",
					clientSideMod.modReadableName(),
					serverSideCompat.equalVersion
			);
			return Result.error(error);
		}
		else if (clientModVersionInt < requiredMinVersionInt) {
			String error = TextUtils.formatString(
					"[{} Mod] Client-Server version mismatch!\n" +
							"Update the client version to {} or higher.",
					clientSideMod.modReadableName(),
					serverSideCompat.minVersion
			);
			return Result.error(error);
		}
		else if (clientModVersionInt > requiredMaxVersionInt) {
			String error = TextUtils.formatString(
					"[{} Mod] Client-Server version mismatch!\n" +
							"Downgrade the client version to {} or lower.",
					clientSideMod.modReadableName(),
					serverSideCompat.maxVersion
			);
			return Result.error(error);
		}
		return Result.PASS;
	}

	private static Result passesOnClient(VersionCompatibility clientSideCompat, VersionTrackedModImpl serverSideMod) {
		if (clientSideCompat.isAny()) return Result.PASS;
		int serverModVersionInt = getModVersionInt(serverSideMod.modVersion());
		Integer requiredMinVersionInt = getModVersionInt(clientSideCompat.minVersion);
		Integer requiredMaxVersionInt = getModVersionInt(clientSideCompat.maxVersion);
		if (requiredMinVersionInt == null) requiredMinVersionInt = -Integer.MAX_VALUE;
		if (requiredMaxVersionInt == null) requiredMaxVersionInt = Integer.MAX_VALUE;

		if (clientSideCompat.equalVersion != null && !clientSideCompat.equalVersion.equalsIgnoreCase(serverSideMod.modVersion())) {
			String error = TextUtils.formatString(
					"[{} Mod] Client-Server version mismatch!\n" +
							"The server is running version ({}).\nClient requires exactly version {}.",
					serverSideMod.modReadableName(),
					serverSideMod.modVersion(),
					clientSideCompat.equalVersion
			);
			return Result.error(error);
		}
		else if (serverModVersionInt < requiredMinVersionInt) {
			String error = TextUtils.formatString(
					"[{} Mod] Client-Server version mismatch!\n" +
							"The server is running an older version ({}).\nClient requires {} or higher.",
					serverSideMod.modReadableName(),
					serverSideMod.modVersion(),
					clientSideCompat.minVersion
			);
			return Result.error(error);
		}
		else if (serverModVersionInt > requiredMaxVersionInt) {
			String error = TextUtils.formatString(
					"[{} Mod] Client-Server version mismatch!\n" +
							"The server is running a newer version ({}).\nClient requires {} or lower.",
					serverSideMod.modReadableName(),
					serverSideMod.modVersion(),
					clientSideCompat.maxVersion
			);
			return Result.error(error);
		}
		return Result.PASS;
	}


	public static String strippedVersionName(String string) {
		if (string.contains("-pre")) {
			string = string.split("-pre")[0];
		}
		if (string.contains("-rc")) {
			string = string.split("-rc")[0];
		}
		string = string.replaceAll("[^\\d.]", ""); //Remove all non-digit and non-dot characters.
		string = string.replaceAll("^\\.+|\\.+$", ""); //Remove all leading or trailing dots.
		while (string.contains("..")) string = string.replace("..",".");

		return string;
	}

	public static Integer getModVersionInt(String string) {
		if (string == null) return null;
		try {

			String originalVersion = string;
			string = strippedVersionName(string);

			String[] parts = string.split("\\.");

			int major = 0;
			int minor = 0;
			int patch = 0;
			int build = 0;
			try {
				major = parts.length > 0 ? Integer.parseInt(parts[0]) : 0;
				minor = parts.length > 1 ? Integer.parseInt(parts[1]) : 0;
				patch = parts.length > 2 ? Integer.parseInt(parts[2]) : 0;
				build = parts.length > 3 ? Integer.parseInt(parts[3]) : 0;
			}catch(Exception e) {
				MatLib.LOGGER.error(TextUtils.formatString("Failed to parse mod version to int: {} (formatted to {})", originalVersion, string));
			}

			if (originalVersion.contains("-pre")) {
				build = -100;
				try {
					build += Integer.parseInt(originalVersion.split("-pre")[1]);
				}catch(Exception ignored) {}
			}

			if (originalVersion.contains("-rc")) {
				build = -10;
				try {
					build += Integer.parseInt(originalVersion.split("-rc")[1]);
				}catch(Exception ignored) {}
			}

            /*
                Supports up to:
                 213 major versions
                 99 minor versions
                 99 patch versions
                 999 build versions

                 So 213.99.99.999 is a valid version for example.

                 Pre-releases act as if 900 build versions are already added, so 100 pre-releases are supported
                 Release candidates act as if 990 build versions are already added, so 10 rc's are supported
             */

			return (major * 10_000_000) + (minor * 100_000) + (patch * 1_000) + build;
		}catch(Exception ignored) {}
		return null;
	}
}
