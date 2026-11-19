package fr.nilpo.pan.config;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * In-memory fixed-window counters, keyed by an arbitrary string (usually
 * "<purpose>|<client ip>"). Good enough for a single-instance app; counters
 * are lost on restart.
 */
@Component
public class RateLimiter {

	private static final int EVICTION_THRESHOLD = 100_000;

	private static final class Window {
		final long start;
		final long lengthMillis;
		int count;

		Window(long start, long lengthMillis) {
			this.start = start;
			this.lengthMillis = lengthMillis;
		}

		boolean expired(long now) {
			return now - start >= lengthMillis;
		}
	}

	private final Map<String, Window> windows = new ConcurrentHashMap<>();

	/** Counts one hit; returns false (without counting) once the limit is reached for the current window. */
	public boolean tryAcquire(String key, int limit, Duration window) {
		evictIfLarge();
		long now = System.currentTimeMillis();
		boolean[] allowed = { false };
		windows.compute(key, (k, current) -> {
			if (current == null || current.expired(now)) {
				current = new Window(now, window.toMillis());
			}
			if (current.count < limit) {
				current.count++;
				allowed[0] = true;
			}
			return current;
		});
		return allowed[0];
	}

	/** True if the key already reached the limit in its current window (does not count a hit). */
	public boolean isLimited(String key, int limit) {
		var current = windows.get(key);
		return current != null && !current.expired(System.currentTimeMillis()) && current.count >= limit;
	}

	/** Counts one hit unconditionally. */
	public void record(String key, Duration window) {
		evictIfLarge();
		long now = System.currentTimeMillis();
		windows.compute(key, (k, current) -> {
			if (current == null || current.expired(now)) {
				current = new Window(now, window.toMillis());
			}
			current.count++;
			return current;
		});
	}

	public void reset(String key) {
		windows.remove(key);
	}

	@Scheduled(fixedDelay = 300_000)
	public void evictExpired() {
		long now = System.currentTimeMillis();
		windows.values().removeIf(w -> w.expired(now));
	}

	private void evictIfLarge() {
		if (windows.size() > EVICTION_THRESHOLD) {
			evictExpired();
		}
	}
}
