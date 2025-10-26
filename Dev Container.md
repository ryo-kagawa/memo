# Dev Container

Dev Containerを利用することで開発環境の統一を図る

## 環境作成方法

### ベースを使用する

1. `Visual Studio Code`でコマンドパレットから`Dev Containers: Add Dev Container Configuration Files...`を選択肢、ベースとなるテンプレートを選択し、追加する`Dev Container Features`を選択する

### 自分で構築する

1. `.devcontainer/devcontainer.json`を作成する
```json
{
  "name": "XXX",
  "image": "mcr.microsoft.com/devcontainers/base",
  "features": {
    // TODO: 必要なものを列挙する
  },
  "postCreateCommand": "bash ".devcontainer/postCreateCommand.sh",
  "customizations": {
    // TODO: 必要なものを列挙する
    "vscode": {
      "extensions": [
        // TODO: 必要なものを列挙する
      ]
    }
  }
}
```
1. `.devcontainer/post-create.sh`を作成する

### 複数使い分けをする

1. `.devcontainer/XXX/devcontainer.json`を作成する

## 環境構築方法

### Windows

1. `Visual Studio Code`をインストール
1. `Visual Studio Code`の拡張機能`Dev Containers`（`ms-vscode-remote.remote-containers`）をインストールする
1. `Docker Desktop for Windows`をインストール
1. 対象リポジトリをWSL2内でクローンする
1. `Visual Studio Code`対象リポジトリを開く
1. `Visual Studio Code`でコマンドパレットから`Dev Containers: Rebuild and Reopen in Container`を選択し、Dev Containerを構築する

### macOS

1. `Visual Studio Code`をインストール
1. `Visual Studio Code`の拡張機能`Dev Containers`（`ms-vscode-remote.remote-containers`）をインストールする
1. `Docker Desktop for Mac`をインストール
1. `Visual Studio Code`対象リポジトリを開く
1. `Visual Studio Code`でコマンドパレットから`Dev Containers: Rebuild and Reopen in Container`を選択し、Dev Containerを構築する

### Linux

1. `Visual Studio Code`をインストール
1. `Visual Studio Code`の拡張機能`Dev Containers`（`ms-vscode-remote.remote-containers`）をインストールする
1. `Docker Desktop for Linux`をインストール
1. `Visual Studio Code`対象リポジトリを開く
1. `Visual Studio Code`でコマンドパレットから`Dev Containers: Rebuild and Reopen in Container`を選択し、Dev Containerを構築する
