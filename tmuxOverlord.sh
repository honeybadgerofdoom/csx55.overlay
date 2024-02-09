##!/bin/sh

hosts=("tehran" "jakarta" "sunlight" "char" "pollock" "bogota" "cockroach" "earth" "hornet" "katydid")

directory="CS555/csx55.overlay"
#start and detach from session
tmux new-session -d -s tmux-messaging-nodes

for i in "${!hosts[@]}"; do
    if [ "$i" -eq 0 ]; then
        #first host, just ssh
        tmux send-keys -t tmux-messaging-nodes "ssh ${hosts[$i]}" C-m "source .bashrc" C-m "cd $directory" C-m "clear" C-m
    else
        #split the window first, then SSH
        tmux split-window -t tmux-messaging-nodes
        tmux select-layout -t tmux-messaging-nodes tiled
        tmux send-keys -t tmux-messaging-nodes "ssh ${hosts[$i]}" C-m "source .bashrc" C-m "cd $directory" C-m "clear" C-m
    fi
done

#attach to the session
tmux attach -t tmux-messaging-nodes
