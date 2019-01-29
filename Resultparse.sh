#!/bin/zsh

for file in `ls $1`
do
  locFile="$1/$file"
  sed -i '/Hash/d' "$locFile"
  sed -i '/Available monitors: 1/d' "$locFile"
  sed -i '/Work Area: x:0, y:0, w:1024, h:768/d' "$locFile"
  sed -i '/convert monitor\[0\] -> glass Screen/d' "$locFile"
  sed -i '/\[x: 0 y: 0 w: 1024 h: 768\]/d' "$locFile"
done