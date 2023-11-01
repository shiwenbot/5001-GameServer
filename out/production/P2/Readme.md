Hope this document can help understand my code:
In my code, after move is called, turn will be moved to the next animal. According to the rule of this game, 
after an animal moves, both this animal and the next can cast spell. Therefore, in my castSpell method, I allow 
both the current turn and previous turn animal cast spell. But I will only update turn if current turn animal 
casts spell to make sure turn is correct. Whether an animal is able to move and cast spell or not is determined 
by boolean moveable and spellable

I use HashMap<Object, Coordinate> to store the location of animal, creature and spell. Class Coordinate has getRow
and getCol method.

In Animal, isStraightLine is used to check whether an animal is moving in a straight line, withCreature checks 
whether a square has animal. And all the move behaviour is in move method, thus there will not be dig, fly and jump
