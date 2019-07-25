import { IReaction } from 'app/shared/model/reaction.model';
import { IUser } from 'app/core/user/user.model';

export const enum ReactionType {
  ROOT = 'ROOT',
  AGREE = 'AGREE',
  DISAGREE = 'DISAGREE',
  COMMENT = 'COMMENT',
  GOOD_QUALITY = 'GOOD_QUALITY',
  BAD_QUALITY = 'BAD_QUALITY'
}

export interface IReaction {
  id?: number;
  title?: string;
  content?: string;
  type?: ReactionType;
  typeLevel?: number;
  supportScore?: number;
  childrenReactions?: IReaction[];
  creator?: IUser;
  parentReaction?: IReaction;
}

export class Reaction implements IReaction {
  constructor(
    public id?: number,
    public title?: string,
    public content?: string,
    public type?: ReactionType,
    public typeLevel?: number,
    public supportScore?: number,
    public childrenReactions?: IReaction[],
    public creator?: IUser,
    public parentReaction?: IReaction
  ) {}
}
