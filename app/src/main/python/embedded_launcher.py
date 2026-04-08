import os
import sys
import threading

import uvicorn


_SERVER_THREAD = None


def start_server(runtime_root: str, data_dir: str, host: str, port: int) -> int:
    global _SERVER_THREAD

    if _SERVER_THREAD is not None and _SERVER_THREAD.is_alive():
        return int(port)

    runtime_root = os.path.abspath(runtime_root)
    data_dir = os.path.abspath(data_dir)
    src_root = os.path.join(runtime_root, "src")

    os.makedirs(data_dir, exist_ok=True)
    os.chdir(runtime_root)
    os.environ["CWS_DATA_DIR"] = data_dir
    os.environ["SERVER_HOST"] = host
    os.environ["SERVER_PORT"] = str(port)

    if runtime_root not in sys.path:
        sys.path.insert(0, runtime_root)
    if src_root not in sys.path:
        sys.path.insert(0, src_root)

    from src.server.main import app

    config = uvicorn.Config(app, host=host, port=int(port), log_level="info")
    server = uvicorn.Server(config)

    def _run() -> None:
        server.run()

    _SERVER_THREAD = threading.Thread(
        target=_run,
        name="cws-uvicorn",
        daemon=True,
    )
    _SERVER_THREAD.start()
    return int(port)
