# Dev Tunnels

Dev Tunnelsを利用することでWebhook等のコールバックをローカルサーバーで実行できるようにする

## CLIをインストール

### Linux
```
curl -sL https://aka.ms/DevTunnelCliInstall | bash
```

## ログイン

### GitHubアカウントを使用してログイン
```
devtunnel user login -g
```

## Dev Tunnelをホスト

### ブラウザでの許可が必要な状態で起動する

```
devtunnel host -p 3000
```

### 誰でもアクセス可能にする

```
devtunnel host -p 3000 --allow-anonymous
```
