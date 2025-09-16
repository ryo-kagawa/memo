package io.github.ryo_kagawa.windows_network_client;

import java.util.Arrays;

import com.sun.jna.platform.win32.WinDef;

@SuppressWarnings("exports")
class DWORDUtil {
	private DWORDUtil() {}

	public static class Operator {
		private Operator() {}

		public static WinDef.DWORD or(WinDef.DWORD value, WinDef.DWORD... values) {
			return new WinDef.DWORD(Arrays.stream(values).mapToLong((x) -> x.longValue()).reduce(value.longValue(), (a, b) -> a | b));
		}
	}
}
