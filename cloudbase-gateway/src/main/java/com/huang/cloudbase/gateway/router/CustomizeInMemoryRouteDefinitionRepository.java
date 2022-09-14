package com.huang.cloudbase.gateway.router;

import cn.hutool.core.collection.CollUtil;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

import static java.util.Collections.synchronizedMap;

public class CustomizeInMemoryRouteDefinitionRepository implements RouteDefinitionRepository {

	private final Map<String, RouteDefinition> routes = synchronizedMap(new LinkedHashMap<>());

	@Override
	public Flux<RouteDefinition> getRouteDefinitions() {
		return Flux.fromIterable(routes.values());
	}

	@Override
	public Mono<Void> save(Mono<RouteDefinition> route) {
		return route.flatMap(r -> {
			if (!StringUtils.hasLength(r.getId())) {
				return Mono.error(new IllegalArgumentException("id may not be empty"));
			}
			routes.put(r.getId(), r);
			return Mono.empty();
		});
	}

	public void refreshRouter(List<RouteDefinition> use) {
		Iterator<Map.Entry<String, RouteDefinition>> iterator = routes.entrySet().iterator();
		while (iterator.hasNext()) {
			String key = iterator.next().getKey();
			if (!CollUtil.contains(use, route -> Objects.equals(route.getId(), key))) {
				iterator.remove();
			}
		}
	}

	@Override
	public Mono<Void> delete(Mono<String> routeId) {
		return routeId.flatMap(id -> {
			if (routes.containsKey(id)) {
				routes.remove(id);
				return Mono.empty();
			}
			return Mono.defer(() -> Mono.error(
				new NotFoundException("RouteDefinition not found: " + routeId)));
		});
	}

}
