package net.mat0u5.matlib.utils.other;

public class Time {
    public static long CONVERT_MILLIS = 1000L;
    public static long CONVERT_TICKS = 50_000L;
    public static long CONVERT_SECONDS = 1_000_000L;
    public static long CONVERT_MINUTES = 60_000_000L;
    public static long CONVERT_HOURS = 3_600_000_000L;
    private Long micros;

    public Time(Long micros) {
        this.micros = micros;
    }

    public Time(int micros) {
        this.micros = (long) micros;
    }

    public boolean isInfinite() {
        return this instanceof TimeInf;
    }

    public long getMicros() {
        return micros;
    }

    public long getMillis() {
        return micros / CONVERT_MILLIS;
    }

    public int getTicks() {
        return (int) (micros / CONVERT_TICKS);
    }

    public int getSeconds() {
        return (int) (micros / CONVERT_SECONDS);
    }

    public int getMinutes() {
        return (int) (micros / CONVERT_MINUTES);
    }

    public int getHours() {
        return (int) (micros / CONVERT_HOURS);
    }

    /**
     * Adds one tick to the current time (50ms).
     * <p>In-place modification.
     */
    public Time tick() {
        return this.add(CONVERT_TICKS);
    }


    /**
     * @param time The time to be added.
     * <p>In-place modification.
     */
    public Time add(Time time) {
        if (time.isInfinite()) return Time.infinite();
        if (micros == null) micros = 0L;
        micros += time.getMicros();
        return this;
    }

    /**
     * @param time Micros to be added.
     * <p>In-place modification.
     */
    public Time add(long time) {
        if (micros == null) micros = 0L;
        micros += time;
        return this;
    }

    /**
     * @param scale Multiplication factor.
     * <p>In-place modification.
     */
    public Time multiply(long scale) {
        if (micros == null) micros = 0L;
        micros *= scale;
        return this;
    }

    /**
     * @return {@code true} if the current time is non-null.
     */
    public boolean isPresent() {
        return micros != null;
    }

    /**
     * @return {@code true} if {@code micros % interval.micros == 0}
     */
    public boolean isMultipleOf(Time interval) {
        if (interval.isInfinite()) return false;
        return micros % interval.getMicros() == 0;
    }

    /**
     * @return A new time containing {@code this - time2}.
     * <p>Out-of-place modification.
     */
    public Time diff(Time time2) {
        return new Time(this.getMicros() - time2.getMicros());
    }

    /**
     * @return {@code true} if this time is larger or equal to the parameter time.
     */
    public boolean isLargerThan(Time time) {
        if (time.isInfinite()) return false;
        return this.getMicros() >= time.getMicros();
    }

     /**
     * @return {@code true} if this time is smaller or equal to the parameter time.
     */
    public boolean isSmallerThan(Time time) {
        if (time.isInfinite()) return true;
        return this.getMicros() <= time.getMicros();
    }

    public Time copy() {
        return new Time(this.micros);
    }

    /**
     * @return Formatted time, example: 30 minutes, or 0:43:20
     */
    public String formatReadable() {
        int seconds = this.getSeconds();
        boolean isNegative = seconds < 0;
        seconds = Math.abs(seconds);

        int hours = seconds / 3600;
        int remainingSeconds = seconds % 3600;
        int minutes = remainingSeconds / 60;
        int secs = remainingSeconds % 60;

        if (hours > 0 && minutes == 0 && secs == 0) {
            return (isNegative ? "-" : "+") + hours + (hours == 1 ? " hour" : " hours");
        } else if (hours == 0 && minutes > 0 && secs == 0) {
            return (isNegative ? "-" : "+") + minutes + (minutes == 1 ? " minute" : " minutes");
        } else if (hours == 0 && minutes == 0 && secs > 0) {
            return (isNegative ? "-" : "+") + secs + (secs == 1 ? " second" : " seconds");
        } else {
            return String.format("%s%d:%02d:%02d", isNegative ? "-" : "+", hours, minutes, secs);
        }
    }

    /**
     * @return Formatted time, example: 0:43:20
     */
    public String formatLong() {
        long totalSeconds = (long) Math.ceil(micros / 1_000_000.0);
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = (totalSeconds % 60);

        return TextUtils.formatString("{}:{}:{}", hours, formatTimeNumber(minutes), formatTimeNumber(seconds));
    }

    /**
     * @return Formatted time, example: 43:20
     */
    public String format() {
        long totalSeconds = (long) Math.ceil(micros / 1_000_000.0);
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = (totalSeconds % 60);
        if (hours == 0) {
            return TextUtils.formatString("{}:{}", formatTimeNumber(minutes), formatTimeNumber(seconds));
        }

        return TextUtils.formatString("{}:{}:{}", hours, formatTimeNumber(minutes), formatTimeNumber(seconds));
    }

    private static String formatTimeNumber(long time) {
        String value = String.valueOf(time);
        while (value.length() < 2) value = "0" + value;
        return value;
    }


    public static Time hours(int hours) {
        return new Time(hours * CONVERT_HOURS);
    }

    public static Time minutes(int minutes) {
        return new Time(minutes * CONVERT_MINUTES);
    }

    public static Time minutes(double minutes) {
        return new Time((long)(minutes * CONVERT_MINUTES));
    }

    public static Time seconds(int seconds) {
        return new Time(seconds * CONVERT_SECONDS);
    }

    public static Time ticks(int ticks) {
        return new Time(ticks * CONVERT_TICKS);
    }

    public static Time millis(long millis) {
        return new Time(millis * CONVERT_MILLIS);
    }

    public static Time micros(long micros) {
        return new Time(micros);
    }

    public static Time now() {
        return new Time(System.currentTimeMillis()* CONVERT_MILLIS);
    }

    public static Time nullTime() {
        return new Time(null);
    }

    public static Time zero() {
        return new Time(0L);
    }

    public static TimeInf infinite() {
        return new TimeInf();
    }


    public static class TimeInf extends Time {
        public TimeInf() {
            super(0L);
        }

        @Override
        public long getMicros() {
            return -1;
        }

        @Override
        public long getMillis() {
            return -1;
        }

        @Override
        public int getTicks() {
            return -1;
        }

        @Override
        public int getSeconds() {
            return -1;
        }

        @Override
        public int getMinutes() {
            return -1;
        }

        @Override
        public int getHours() {
            return -1;
        }

        @Override
        public Time tick() {
            return this;
        }

        @Override
        public Time add(Time time) {
            return this;
        }

        @Override
        public Time add(long time) {
            return this;
        }

        @Override
        public Time multiply(long scale) {
            return this;
        }

        @Override
        public boolean isPresent() {
            return true;
        }

        @Override
        public boolean isMultipleOf(Time interval) {
            return true;
        }

        @Override
        public Time diff(Time time2) {
            return Time.infinite();
        }

        @Override
        public boolean isLargerThan(Time time2) {
            return true;
        }

        @Override
        public boolean isSmallerThan(Time time2) {
            return false;
        }

        @Override
        public Time copy() {
            return Time.infinite();
        }

        @Override
        public String formatReadable() {
            return "infinite";
        }

        @Override
        public String formatLong() {
            return "infinite";
        }

        @Override
        public String format() {
            return "infinite";
        }
    }
}
