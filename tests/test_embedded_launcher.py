import os
import sys
import types
from pathlib import Path


PYTHON_SRC = Path(__file__).resolve().parents[1] / "app" / "src" / "main" / "python"
if str(PYTHON_SRC) not in sys.path:
    sys.path.insert(0, str(PYTHON_SRC))

import embedded_launcher


class DummyThread:
    def __init__(self, target, name, daemon):
        self._target = target
        self.started = False

    def start(self):
        self.started = True
        self._target()

    def is_alive(self):
        return self.started


def test_start_server_sets_env_and_boots_uvicorn(tmp_path, monkeypatch):
    runtime_root = tmp_path / "runtime"
    data_root = tmp_path / "data"
    runtime_root.mkdir()
    (runtime_root / "src").mkdir()

    monkeypatch.setitem(sys.modules, "src.server.main", types.SimpleNamespace(app="APP"))

    calls = {}

    class FakeServer:
        def __init__(self, config):
            calls["config"] = config

        def run(self):
            calls["ran"] = True

    monkeypatch.setattr(
        embedded_launcher.uvicorn,
        "Config",
        lambda app, host, port, log_level: {
            "app": app,
            "host": host,
            "port": port,
            "log_level": log_level,
        },
    )
    monkeypatch.setattr(embedded_launcher.uvicorn, "Server", FakeServer)
    monkeypatch.setattr(embedded_launcher.threading, "Thread", DummyThread)
    monkeypatch.setattr(embedded_launcher, "_SERVER_THREAD", None)

    returned_port = embedded_launcher.start_server(
        str(runtime_root),
        str(data_root),
        "127.0.0.1",
        18002,
    )

    assert returned_port == 18002
    assert os.environ["CWS_DATA_DIR"] == str(data_root)
    assert os.environ["SERVER_HOST"] == "127.0.0.1"
    assert os.environ["SERVER_PORT"] == "18002"
    assert os.getcwd() == str(runtime_root)
    assert str(runtime_root / "src") in sys.path
    assert calls["config"]["app"] == "APP"
    assert calls["config"]["port"] == 18002
    assert calls["ran"] is True
