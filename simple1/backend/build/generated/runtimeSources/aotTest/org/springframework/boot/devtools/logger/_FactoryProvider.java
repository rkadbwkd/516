package org.springframework.boot.devtools.logger;

public abstract class _FactoryProvider {
  public static DevToolsLogFactory.Listener devToolsLogFactoryListener() {
    return new DevToolsLogFactory.Listener();
  }
}
