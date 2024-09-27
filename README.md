# 물고기 추가
원하는 물고기를 추가하여 확률적으로 낚을 수 있게 해주는 시스템입니다.

## 다운로드
* 플러그인 다운로드는 [릴리즈](https://github.com/CosinePlugin/HQFishAdder/releases)에서 가능합니다.

## 디스코드
* 모든 문의, 질문, 개발 요청은 [디스코드](https://discord.gg/hUkaca9ZQu)에서 해주시길 바랍니다.

## 필수 플러그인
*  [HQFramework](https://github.com/HQService/HQFramework)

## 버전
* 1.17.1+

## 관리자 명령어
* /물고기관리

## 이벤트
* 물고기를 잡았을 때 발동되는 이벤트입니다.
```java
public class FishListener implements Listener {
    @EventHandler
    public void onHQFishCaught(HQFishCaughtEvent event) {
        ItemStack fish = event.getFish();
        String fishDisplayName = event.getFishDisplayName();
        double chance = event.getChance();
    }
}
```

```kotlin
class FishListener : Listener {
    @EventHandler
    fun onHQFishCaught(event: HQFishCaughtEvent) {
        val fish = event.fish
        val fishDisplayName = event.fishDisplayName
        val chance = event.chance
    }
}
```

```yml
import:
    kr.cosine.fishadder.event.HQFishCaughtEvent
    
on HQFishCaughtEvent:
    set {_fish} to event.getFish() # ItemStack (아이템)
    set {_fishDisplayName} to event.getFishDisplayName() # String (아이템 이름)
    set {_chance} to event.getChance() # Double (확률)
```
