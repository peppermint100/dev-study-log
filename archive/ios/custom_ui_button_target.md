# 커스텀 버튼을 만들 때 addTarget으로 메소드 할당이 안되는 경우


```swift
private func buildUI() {
        addSubview(buttonStackView)
        buttonStackView.addArrangedSubview(iconImageView)
        buttonStackView.addArrangedSubview(buttonTitleLabel)
        
        buttonStackView.isUserInteractionEnabled = false
        buttonTitleLabel.textColor = .systemBlue
        buttonTitleLabel.font = UIFont.boldSystemFont(ofSize: 18)
        iconImageView.contentMode = .scaleAspectFit
    }
```

UIButton을 덮은 스택뷰에 `isUserInteractionEnabled` 속성을 false로 주면 버튼위에 있는 그 아래 레이어에 있는 버튼이 클릭 되도록 해주어야 한다