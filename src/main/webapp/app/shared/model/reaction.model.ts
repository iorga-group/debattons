import { IReaction } from 'app/shared/model/reaction.model';
import { IUser } from 'app/core/user/user.model';

export interface IReaction {
    id?: number;
    title?: string;
    content?: string;
    childrenReactions?: IReaction[];
    creator?: IUser;
    parentReaction?: IReaction;
}

export class Reaction implements IReaction {
    constructor(
        public id?: number,
        public title?: string,
        public content?: string,
        public childrenReactions?: IReaction[],
        public creator?: IUser,
        public parentReaction?: IReaction
    ) {}
}
