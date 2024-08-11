# Contributing

あなたのコントリビュートをお待ちしております！

## ステップバイステップのコントリビュート方法

### 1. ソースコードをダウンロードする

まずリポジトリをローカルにダウンロードして実行することで、アプリを試してみましょう。アプリの使い方や機能を理解するのに役立ちます。
リポジトリの右上にある `Fork` ボタンをクリックしてください。これであなたのアカウントにリポジトリがコピーされます。

![image](https://github.com/user-attachments/assets/39aa034e-052f-4084-b864-a3214841752c)

そのあと、以下のコマンドを実行してください。

```bash
git clone https://github.com/[あなたのアカウント]/conference-app-2024
```

これでリポジトリがあなたのPCにダウンロードされます。

### 2. アプリを実行してみる

Android Studioを開いて、"Open"からダウンロードしたリポジトリを開いてください。Android Studioは[こちら](https://developer.android.com/studio)からダウンロードできます。最新のバージョンを使ってください。

リポジトリを開くと、Syncが始まります。Gradleの同期が終わるまでお待ちください。

`app-android` モジュールをビルドして実行します。Android Studio上の実行ボタンをクリックしてください。

![image](https://github.com/user-attachments/assets/66f3f0c8-ee18-4961-9c3b-7a808cd2a3b4)

### 3. タスクを見つける

タスク管理にGitHub Issueを使っています。こちらでコントリビュートしたいIssueをお探しください。[`contributions welcome` または `easy`のラベルがついているIssue](https://github.com/DroidKaigi/conference-app-2024/issues?q=is%3Aissue+is%3Aopen+label%3A%22difficulty%3Aeasy+%F0%9F%8C%B1%22+label%3A%22contributions+welcome%22+)は、初めてのコントリビュートにおすすめです。

IssueがないPull Requestでも大丈夫です。その場合はPull Requestに理由、原因、解決策などの詳細をご記入ください。

### 4. コントリビュートを始める

もし取り組みたいタスクを見つけたら、他の人と重複して作業しないようにするためIssueに ":raising_hand:" などのコメントをしてください。
なるべく早くいただいたコメントにリアクションしますが、Issueにコメントを書いたらタスクに着手していただいて構いません！

### 5. 開発する

アプリのコードを変更し、開発しましょう！

今年採用しているUIツールキットのJetpack Composeについて学びたい方は以下が参考になるので、確認してみてください。
https://developer.android.com/courses/jetpack-compose/course

コードフォーマットのコマンドは以下の通りです。Android Studioでこのドキュメントを開いて、左側の実行ボタンから実行できます。

```bash
./gradlew detekt --auto-correct
```

### 6. プルリクエストを作成する

変更が完了したら、プルリクエストを作成してください。
gitのコミットとプッシュを行い、GitHubのUI ( https://github.com/[あなたのアカウント]/conference-app-2024 )からプルリクエストを作成します。

私たちはIssue、コメント、レビューでは英語を使います。

差し支えありませんでしたら英語のご使用をお願いいたしますが、日本語でも大丈夫です。
恐れ入りますが、本リポジトリでは英語と日本語のサポートを行わせていただきます。
※ご参考までに、DroidKaigiの運営メンバーは日本語スピーカーと英語スピーカーから構成されています。

非英語圏のみなさまへ、DroidKaigiの運営メンバーもみな英語が得意というわけではありません。英語でのコミュニケーションを恐れず挑戦してみてください！ :smile:

### 7. コードレビュー、マージ

プルリクエストの作成が終わるとコードレビューが始まります。修正をお願いすることがあれば、差分にインラインコメントがつきますので適宜確認をしましょう。修正が完了したらマージされます。 👍

## 議論や提案の方法

Issueが望ましいですが、もし実装やリファクタリングのための具体的なアイデアなどがありましたらPull Requestも歓迎です。

## NOTE

分からないことがありましたら何でも聞いてください！

コントリビュートを楽しみ、多くのことを学び、知識を共有し、DroidKaigiを楽しみましょう！
